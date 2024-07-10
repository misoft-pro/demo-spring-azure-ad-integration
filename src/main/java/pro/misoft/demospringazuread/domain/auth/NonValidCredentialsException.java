package pro.misoft.demospringazuread.domain.auth;

public class NonValidCredentialsException extends RuntimeException {
    public NonValidCredentialsException(String msg) {
        super(msg);
    }
}
