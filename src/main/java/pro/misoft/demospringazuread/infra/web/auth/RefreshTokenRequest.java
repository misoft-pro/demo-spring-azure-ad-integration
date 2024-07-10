package pro.misoft.demospringazuread.infra.web.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;


public record RefreshTokenRequest(@JsonProperty("refreshToken") @NotEmpty String refreshToken) {
}
