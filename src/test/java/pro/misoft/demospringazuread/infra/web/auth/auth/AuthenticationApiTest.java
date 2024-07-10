package pro.misoft.demospringazuread.infra.web.auth.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import pro.misoft.demospringazuread.domain.user.PasswordObjectMother;
import pro.misoft.demospringazuread.domain.user.PhoneNumber;
import pro.misoft.demospringazuread.domain.user.PhoneNumberObjectMother;
import pro.misoft.demospringazuread.domain.user.User;
import pro.misoft.demospringazuread.infra.errorhandling.ApiError;
import pro.misoft.demospringazuread.infra.web.AbstractApiTest;
import pro.misoft.demospringazuread.infra.web.UserApiClient;
import pro.misoft.demospringazuread.infra.web.auth.AuthTokenResponse;
import pro.misoft.demospringazuread.infra.web.auth.CreateTokenRequest;
import pro.misoft.demospringazuread.infra.web.user.CreateUserRequest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AuthenticationApiTest extends AbstractApiTest {

    private static final String VALID_PASSWORD = "!Aa123456";

    @Test
    void shouldReturnNewAuthTokenWhenPassValidUsernameAndPassword() {
        var sutUser = createTokenRequest();

        AuthTokenResponse auth = AuthApiClient.createToken(sutUser.phoneNumber(), sutUser.password());

        assertThat(auth.idToken()).isNotBlank();
        assertThat(auth.expiresInSeconds()).isPositive();
        assertThat(auth.refreshToken()).isNotBlank();
    }

    @Test
    void shouldReturnBadRequestWhenPassNonExistingUsernameAndPassword() {
        ApiError apiError = AuthApiClient.createToken(PhoneNumberObjectMother.randomValidPhoneNumber(), VALID_PASSWORD, BAD_REQUEST);

        assertThat(apiError.errorMessage()).isEqualTo("You've entered incorrect phone number or password");
    }

    @Test
    void shouldReturnBadRequestWhenNonValidCredentials() {
        PhoneNumber phone = PhoneNumberObjectMother.randomValidPhoneNumber();
        UserApiClient.createUser(new CreateUserRequest(phone, VALID_PASSWORD));
        createTokenRequest();

        String nonMatchingPassword = PasswordObjectMother.randomPassword();
        ApiError apiError = AuthApiClient.createToken(phone, nonMatchingPassword, BAD_REQUEST);

        assertThat(apiError.errorMessage()).isEqualTo("You've entered incorrect phone number or password");
    }

    @Test
    void shouldReturnBadRequestWhenNonValidPasswordFormat() {
        var sutUser = createTokenRequest();

        ApiError apiError = AuthApiClient.createToken(sutUser.phoneNumber(), "", BAD_REQUEST);

        assertThat(apiError.errorMessage()).isEqualTo("Input fields contain errors");
        assertThat(apiError.subErrors()).hasSize(1);
        assertThat(apiError.subErrors().get(0).message()).isEqualTo("Enter your password");
    }

    @Test
    void shouldReturnNewAuthTokenWhenPassValidRefreshToken() {
        var sutUser = createTokenRequest();
        AuthTokenResponse auth = AuthApiClient.createToken(sutUser.phoneNumber(), sutUser.password());
        AuthTokenResponse resp = AuthApiClient.refreshToken(auth.refreshToken());

        assertThat(resp.idToken()).isNotEqualTo(auth.idToken());
        assertThat(resp.expiresInSeconds()).isPositive();
        assertThat(resp.refreshToken()).isNotBlank();

        User user = UserApiClient.getUserProfile(resp.idToken(), HttpStatus.OK, User.class);
        assertThat(user.getLastName()).isNotEmpty();
    }

    @Test
    void shouldReturnBadRequestWhenPassEmptyRefreshToken() {
        ApiError resp = AuthApiClient.refreshToken("", BAD_REQUEST);

        assertThat(resp.errorMessage()).isEqualTo("Input fields contain errors");
    }

    @Test
    void shouldReturnBadRequestWhenRefreshTokenRequestedAfterRevokeAccess() {
        var sutUser = createTokenRequest();

        var result = AuthApiClient.createToken(sutUser.phoneNumber(), sutUser.password());
        AuthApiClient.revokeAccess(result.idToken());

        ApiError resp = AuthApiClient.refreshToken(result.refreshToken(), HttpStatus.BAD_REQUEST);
        assertThat(resp.errorMessage()).isEqualTo("You've entered incorrect phone number or password");
    }

    private CreateTokenRequest createTokenRequest() {
        return new CreateTokenRequest(PhoneNumberObjectMother.randomValidPhoneNumber(), VALID_PASSWORD);
    }
}