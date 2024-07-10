package pro.misoft.demospringazuread.domain.user;

public final class UserConstraints {
    public static final String FIRST_NAME_REGEX = "^[A-Z|a-z|0-9| '-]{2,20}$";
    public static final String LAST_NAME_REGEX = FIRST_NAME_REGEX;
    public static final String FULL_NAME_REGEX = "^[A-Z|a-z|0-9| '-]{2,20}\\s[A-Z|a-z|0-9| '-]{2,20}$";

    public static final String PASSWORD_REGEX = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*\\-_!+=\\[\\]{}|\\\\:',.?/`~\\\"();<> ])[\\da-zA-Z@#$%^&*\\-_!+=\\[\\]{}|\\\\:',.?/`~\\\"();<> ]{8,}"; //NOSONAR

    public static final String PIN_CODE_REGEX = "\\d{6}";

    private UserConstraints() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
