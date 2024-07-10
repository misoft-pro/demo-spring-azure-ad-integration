package pro.misoft.demospringazuread.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Address(
        @JsonProperty("street") @NotBlank String street,
        @JsonProperty("code") @NotNull @Min(2000) @Max(5000) Integer code,
        @JsonProperty("city") @NotBlank String city,
        @JsonProperty("extraDetails") String extraDetails
) {
}