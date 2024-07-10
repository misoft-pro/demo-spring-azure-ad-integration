package pro.misoft.demospringazuread.infra.web.auth.auth;

import org.springframework.http.HttpStatus;
import pro.misoft.demospringazuread.domain.user.PhoneNumber;
import pro.misoft.demospringazuread.infra.errorhandling.ApiError;
import pro.misoft.demospringazuread.infra.web.auth.AuthTokenResponse;
import pro.misoft.demospringazuread.infra.web.auth.CreateTokenRequest;
import pro.misoft.demospringazuread.infra.web.auth.RefreshTokenRequest;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static pro.misoft.demospringazuread.infra.web.RestClient.post;


public class AuthApiClient {
    private static final String PREFIX = "/v1/tokens";

    public static AuthTokenResponse createToken(PhoneNumber phoneNumber, String password) {
        return post(PREFIX, new CreateTokenRequest(phoneNumber, password), HttpStatus.CREATED, AuthTokenResponse.class);
    }

    public static ApiError createToken(PhoneNumber phoneNumber, String password, HttpStatus status) {
        return post(PREFIX, new CreateTokenRequest(phoneNumber, password), status, ApiError.class);
    }

    public static AuthTokenResponse refreshToken(String refreshToken) {
        return post(PREFIX + "/refresh", new RefreshTokenRequest(refreshToken), HttpStatus.CREATED, AuthTokenResponse.class);
    }

    public static ApiError refreshToken(String refreshToken, HttpStatus status) {
        return post(PREFIX + "/refresh", new RefreshTokenRequest(refreshToken), status, ApiError.class);
    }

    public static void revokeAccess(String accessToken) {
        post(PREFIX + "/revoke", authHeader(accessToken), null, HttpStatus.OK, Void.TYPE);
    }

    public static Map<String, String> authHeader(String token) {
        return Map.of(AUTHORIZATION, "Bearer " + token);
    }
}
