package pro.misoft.demospringazuread.infra.web.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import pro.misoft.demospringazuread.domain.user.PhoneNumber;

import static pro.misoft.demospringazuread.domain.user.UserConstraints.PASSWORD_REGEX;

public record CreateTokenRequest(@JsonProperty("phoneNumber") @NotNull @Valid PhoneNumber phoneNumber,
                                 @JsonProperty("password")
                                 @Pattern(regexp = PASSWORD_REGEX, message = "{errors.password.pattern}")
                                 @NotEmpty(message = "{errors.password.notempty}") String password) {
}
