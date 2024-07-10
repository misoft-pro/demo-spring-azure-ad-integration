package pro.misoft.demospringazuread.domain.auth;

public interface TokenDecoder {

    /**
     * No token validation happens in this method. All token validation happens on Azure API Management cloud service which is responsible for user authentication.
     * Only token decoding and json parsing happens here to find oid (externalUserId) field
     * @param idToken
     * @return externalUserId field from user domain
     */
    String getExternalUserIdFromAuthToken(String idToken);
}
