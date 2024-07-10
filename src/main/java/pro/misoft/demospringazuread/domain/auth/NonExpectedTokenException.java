package pro.misoft.demospringazuread.domain.auth;

public class NonExpectedTokenException extends RuntimeException {
    public NonExpectedTokenException(String msg, Exception e) {
        super(msg, e);
    }

    public NonExpectedTokenException(String msg) {
        super(msg);
    }
}
