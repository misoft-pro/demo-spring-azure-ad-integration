package pro.misoft.demospringazuread.infra.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pro.misoft.demospringazuread.domain.auth.AuthToken;
import pro.misoft.demospringazuread.domain.auth.NonExpectedTokenException;
import pro.misoft.demospringazuread.domain.auth.TokenCreator;
import pro.misoft.demospringazuread.domain.auth.TokenDecoder;
import pro.misoft.demospringazuread.domain.user.PhoneNumber;
import pro.misoft.demospringazuread.infra.aad.AadUserConverter;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Component("tokenService")
public class JwtTokenService implements TokenDecoder, TokenCreator {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);
    private static final String INVALID_TOKEN_STRUCTURE_MSG = "Invalid token structure: %s";
    private static final String TOKEN_BODY_KEY = "body";
    private static final String TOKEN_CLAIM_TID = "tid";
    private static final String TOKEN_CLAIM_AUD = "aud";
    private final String mobileClientSecret;
    private final String tenantName;
    private final ObjectMapper objectMapper;
    private final String mobileClientId;
    private final String tenantId;
    private final RestTemplate restTemplate;

    public JwtTokenService(@Value("${azure.activedirectory.mobile-client-id}") String mobileClientId, @Value("${azure.activedirectory.mobile-client-secret}") String mobileClientSecret,
                           @Value("${azure.activedirectory.tenant-id}") String tenantId, @Value("${azure.activedirectory.tenant-name}") String tenantName, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.mobileClientSecret = mobileClientSecret;
        this.tenantName = tenantName;
        this.objectMapper = objectMapper;
        this.mobileClientId = mobileClientId;
        this.tenantId = tenantId;
        this.restTemplate = restTemplate;
    }

    @Override
    public String getExternalUserIdFromAuthToken(String idToken) {
        Map<String, Object> map = getTokenComponents(idToken);
        if (map.containsKey(TOKEN_BODY_KEY)) {
            Map<String, Object> tokenBody = (Map<String, Object>) map.get(TOKEN_BODY_KEY);
            if (!tokenBody.containsKey(TOKEN_CLAIM_TID) || !tenantId.equals(tokenBody.get(TOKEN_CLAIM_TID))) {
                throw new NonExpectedTokenException("JWT token came from not expected Active Directory tenant id=" + tenantId);
            }
            if (!tokenBody.containsKey(TOKEN_CLAIM_AUD) || !mobileClientId.equals(tokenBody.get(TOKEN_CLAIM_AUD))) {
                throw new NonExpectedTokenException("JWT token came from not expected client application = " + tokenBody.get(TOKEN_CLAIM_AUD));
            }
            String adObjectId = "oid"; //external user id
            if (tokenBody.containsKey(adObjectId)) {
                String externalUserId = (String) tokenBody.get(adObjectId);
                log.debug("JWT token parsed successfully to externalUserId={}", externalUserId);
                return externalUserId;
            } else {
                throw new NonExpectedTokenException("OID (externalUserId) is missed in JWT token");
            }
        } else {
            throw new NonExpectedTokenException(INVALID_TOKEN_STRUCTURE_MSG.formatted("token body is missed"));
        }
    }

    @Override
    public AuthToken createToken(PhoneNumber phoneNumber, String password) {
        log.info("It's about to create JWT token in AAD for a user [{}] ", phoneNumber.masked());
        AuthToken result = createTokenRequest(phoneNumber, password);
        log.info("JWT token is successfully created in AAD for a user[{}]", phoneNumber.masked());
        return result;
    }

    @Override
    public AuthToken refreshToken(String refreshToken) {
        log.info("It's about to refresh JWT token");
        AuthToken result = refreshTokenRequest(refreshToken);
        log.info("JWT token is successfully refreshed");
        return result;
    }

    private AuthToken refreshTokenRequest(String refreshToken) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_secret", mobileClientSecret);
        map.add("grant_type", "refresh_token");
        map.add("client_id", mobileClientId);
        map.add("refresh_token", refreshToken);
        return commonRequest(map);
    }

    private AuthToken createTokenRequest(PhoneNumber phoneNumber, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_secret", mobileClientSecret);
        map.add("grant_type", "password");
        map.add("client_id", mobileClientId);
        map.add("scope", "openid email profile offline_access");
        map.add("username", AadUserConverter.userPrincipalName(tenantName, phoneNumber));
        map.add("password", password);
        return commonRequest(map);
    }

    private AuthToken commonRequest(MultiValueMap<String, String> formFields) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formFields, headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://login.microsoftonline.com:443/{tenantId}/oauth2/v2.0/token");
        URI uri = builder.buildAndExpand(tenantId).toUri();
        ResponseEntity<AuthToken> rs = restTemplate.postForEntity(uri, request, AuthToken.class);
        return rs.getBody();
    }


    private Map<String, Object> getTokenComponents(String idToken) {
        Base64.Decoder decoder = Base64.getDecoder();
        StringTokenizer tokenizer = new StringTokenizer(idToken, ".");
        Map<String, Object> tokenHeader = new HashMap<>();
        Map<String, Object> tokenBody = new HashMap<>();
        String signatureJws = "";
        Map<String, Object> tokenMapParts = new HashMap<>();
        // decode the 3 parts of the jwt token
        int i = 0;
        while (tokenizer.hasMoreElements()) {
            if (i == 0) {
                tokenHeader = string2JSONMap(new String(decoder.decode(tokenizer.nextToken())));
            } else if (i == 1) {
                tokenBody = string2JSONMap(new String(decoder.decode(tokenizer.nextToken())));
            } else {
                signatureJws = tokenizer.nextToken();
            }
            i++;
        }
        if (tokenHeader.isEmpty() || tokenBody.isEmpty() || signatureJws.isEmpty()) {
            throw new NonExpectedTokenException(INVALID_TOKEN_STRUCTURE_MSG.formatted("3 jwt parts are not in place"));
        }
        tokenMapParts.put("header", tokenHeader);
        tokenMapParts.put(TOKEN_BODY_KEY, tokenBody);
        tokenMapParts.put("signature", signatureJws);
        return tokenMapParts;
    }

    private Map<String, Object> string2JSONMap(String json) {
        Map<String, Object> map;
        try {
            map = objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new NonExpectedTokenException("Could not parse jwt token", e);
        }
        return map;
    }
}
