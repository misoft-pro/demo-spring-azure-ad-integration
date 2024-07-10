package pro.misoft.demospringazuread.infra.web;

import org.springframework.http.HttpStatus;
import pro.misoft.demospringazuread.domain.common.Id;
import pro.misoft.demospringazuread.domain.user.User;
import pro.misoft.demospringazuread.infra.web.auth.auth.AuthApiClient;
import pro.misoft.demospringazuread.infra.web.user.CreateUserRequest;

import static pro.misoft.demospringazuread.infra.web.RestClient.get;
import static pro.misoft.demospringazuread.infra.web.RestClient.post;

public class UserApiClient {

    private static final String PREFIX = "/v1/users";

    public static <R> R createUser(CreateUserRequest request, HttpStatus responseStatus, Class<R> responseClass) {
        return post(PREFIX, request, responseStatus, responseClass);
    }

    public static Id createUser(CreateUserRequest request) {
        return createUser(request, HttpStatus.CREATED, Id.class);
    }

    public static User getUserProfile(String token, HttpStatus httpStatus, Class<User> userClass) {
        return get(PREFIX, AuthApiClient.authHeader(token), null,  userClass);
    }
}
