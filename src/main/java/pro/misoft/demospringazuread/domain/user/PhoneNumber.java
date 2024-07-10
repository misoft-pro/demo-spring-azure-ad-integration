package pro.misoft.demospringazuread.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import pro.misoft.demospringazuread.domain.common.ConstraintValidator;

import java.util.Objects;

public final class PhoneNumber {

    @JsonProperty("countryCode")
    @NotBlank
    @Size(min = 2, max = 5)
    private final String countryCode;

    @JsonProperty("number")
    @Pattern(regexp = "[\\d]{7,10}", message = "{errors.phonenumber.number.pattern}")
    @NotEmpty(message = "{errors.phonenumber.number.notempty}")
    private final String number;

    public static PhoneNumber newPhoneNumber(String countryCode, String number) {
        PhoneNumber phoneNumber = new PhoneNumber(countryCode, number);
        validate(phoneNumber);
        return phoneNumber;
    }

    public static void validate(PhoneNumber pn) {
        ConstraintValidator.validate(pn);
    }

    PhoneNumber(String countryCode, String number) {
        this.countryCode = countryCode;
        this.number = number;
    }

    private PhoneNumber() {
        this.countryCode = null;
        this.number = null;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getNumber() {
        return number;
    }

    public String masked() {
        int maskCount = 4;
        String maskString = StringUtils.repeat("*", maskCount);
        String phone = e164formatted();
        return StringUtils.overlay(phone, maskString, maskCount, maskCount+maskCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PhoneNumber) obj;
        return Objects.equals(this.countryCode, that.countryCode) &&
                Objects.equals(this.number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, number);
    }

    @Override
    public String toString() {
        return e164formatted();
    }

    public String e164formatted() {
        return countryCode + number;
    }

    static PhoneNumber nonValidPhoneNumber() {
        return new PhoneNumber("+380", "123");
    }

    static PhoneNumber emptyPhoneNumber() {
        return new PhoneNumber();
    }
}
