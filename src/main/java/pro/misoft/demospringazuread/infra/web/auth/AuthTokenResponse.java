package pro.misoft.demospringazuread.infra.web.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthTokenResponse(@JsonProperty("idToken") String idToken, @JsonProperty("expiresInSeconds") Integer expiresInSeconds,
                                @JsonProperty("refreshToken") String refreshToken) {
}
