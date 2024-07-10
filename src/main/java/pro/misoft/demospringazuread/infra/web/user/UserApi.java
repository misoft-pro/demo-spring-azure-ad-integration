package pro.misoft.demospringazuread.infra.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pro.misoft.demospringazuread.domain.common.Id;
import pro.misoft.demospringazuread.domain.user.IdentityService;
import pro.misoft.demospringazuread.domain.user.User;
import pro.misoft.demospringazuread.domain.user.UserNotFoundException;
import pro.misoft.demospringazuread.infra.auth.ApiSecurityIsRequired;
import pro.misoft.demospringazuread.infra.auth.FromAuthToken;
import pro.misoft.demospringazuread.infra.errorhandling.ApiError;

@RestController
@Tag(name = "User", description = "The User API. Contains operations to manage users")
@RequestMapping(path = "/v1/users", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserApi {

    private static final Logger log = LoggerFactory.getLogger(UserApi.class);

    private final IdentityService userService;

    public UserApi(IdentityService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create user and set up password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User is created successfully"),
            @ApiResponse(responseCode = "400", description = "Passwords should contain at least 8 characters with 1 number, 1 upper case letter, 1 lower case letter, 1 special character",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))})
    })
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Id createUser(@RequestBody CreateUserRequest request) {
        Id userId = userService.createUser(request.phone(), request.password());
        log.info("User is created with id={}", userId.value());
        return userId;
    }

    @Operation(summary = "Get user profile information")
    @ApiSecurityIsRequired
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile is returned successfully",
                    content = {@Content(schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}),
    })
    @PutMapping(value = "profile")
    public User getUserProfile(@FromAuthToken String userId) throws UserNotFoundException {
        return userService.getUser(userId);
    }
}
