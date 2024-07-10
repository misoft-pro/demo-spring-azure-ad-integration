package pro.misoft.demospringazuread.domain.user;

import pro.misoft.demospringazuread.domain.common.CodeGenerator;


public class PhoneNumberObjectMother {

    public static final PhoneNumber VALID_PHONE_NUMBER = PhoneNumber.newPhoneNumber("+380", "" + CodeGenerator.generate(8, true));
    public static final String INVALID_PHONE_NUMBER = "{\"countryCode\": \"+380\", \"number\": \"123\"}";

    public static PhoneNumber randomValidPhoneNumber() {
        return PhoneNumber.newPhoneNumber("+380", "" + CodeGenerator.generate(8, true));
    }

    public static PhoneNumber newPhoneNumber(String countryCode, String number) {
        return new PhoneNumber(countryCode, number);
    }

    public static PhoneNumber invalidValidPhoneNumber() {
        return new PhoneNumber("+380", "" + CodeGenerator.generate(6, true));
    }
}