package pro.misoft.demospringazuread.domain.auth;

public class AuthUserNotFoundException extends RuntimeException {
    public AuthUserNotFoundException(String msg) {
        super(msg);
    }
}
