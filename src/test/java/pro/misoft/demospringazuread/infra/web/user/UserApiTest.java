package pro.misoft.demospringazuread.infra.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import pro.misoft.demospringazuread.domain.common.Id;
import pro.misoft.demospringazuread.domain.user.PhoneNumberObjectMother;
import pro.misoft.demospringazuread.infra.web.AbstractApiTest;
import pro.misoft.demospringazuread.infra.web.UserApiClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserApiTest extends AbstractApiTest {

    private static final String VALID_PASSWORD = "!Aa12345";

    @Test
    void shouldCreateUserSuccess() {

        var request = new CreateUserRequest(PhoneNumberObjectMother.randomValidPhoneNumber(), VALID_PASSWORD);

        Id userId = UserApiClient.createUser(request, HttpStatus.CREATED, Id.class);

        assertThat(userId.value()).isNotNull();
    }
}