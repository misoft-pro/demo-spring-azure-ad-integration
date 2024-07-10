package pro.misoft.demospringazuread.domain.auth;


import pro.misoft.demospringazuread.domain.user.PhoneNumber;

public interface TokenCreator {

    AuthToken createToken(PhoneNumber phoneNumber, String password);

    AuthToken refreshToken(String refreshToken);
}
