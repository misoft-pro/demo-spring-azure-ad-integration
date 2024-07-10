package pro.misoft.demospringazuread.domain.user;


import pro.misoft.demospringazuread.domain.common.CodeGenerator;

public class PasswordObjectMother {

    public static String ANY_VALID_PASSWORD = "!Bb12345";

    public static String randomPassword() {
        return "!Bb" + CodeGenerator.generateNumbers(5);
    }
}