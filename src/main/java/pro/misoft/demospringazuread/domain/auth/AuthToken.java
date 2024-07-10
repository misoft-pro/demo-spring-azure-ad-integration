package pro.misoft.demospringazuread.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthToken(@JsonProperty("id_token") String idToken, @JsonProperty("expires_in") Integer expiresInSeconds,
                        @JsonProperty("refresh_token") String refreshToken) {
}
