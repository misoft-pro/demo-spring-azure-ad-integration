package pro.misoft.demospringazuread.infra.web.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pro.misoft.demospringazuread.domain.auth.AuthToken;
import pro.misoft.demospringazuread.domain.auth.TokenCreator;
import pro.misoft.demospringazuread.domain.common.ConstraintValidator;
import pro.misoft.demospringazuread.domain.user.IdentityService;
import pro.misoft.demospringazuread.domain.user.UserNotFoundException;
import pro.misoft.demospringazuread.infra.auth.ApiSecurityIsRequired;
import pro.misoft.demospringazuread.infra.auth.FromAuthToken;
import pro.misoft.demospringazuread.infra.errorhandling.ApiError;

@RestController
@Tag(name = "Authentication", description = "The Authentication API. Contains operations to exchange user credentials on the auth token")
@RequestMapping(path = "/v1/tokens", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthenticationApi {

    private final TokenCreator tokenCreator;
    private final IdentityService identityService;

    public AuthenticationApi(TokenCreator tokenCreator, IdentityService identityService) {
        this.tokenCreator = tokenCreator;
        this.identityService = identityService;
    }

    @Operation(summary = "Create new auth token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "JWT token is created successfully",
                    content = {@Content(schema = @Schema(implementation = AuthToken.class))}),
            @ApiResponse(responseCode = "400", description = "Passwords should contain at least 8 characters with 1 number, 1 upper case letter, 1 lower case letter, 1 special character",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "There is no active user with provided phone number",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}),
    })
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public AuthTokenResponse createToken(@RequestBody @Valid CreateTokenRequest request){
        AuthToken domain = tokenCreator.createToken(request.phoneNumber(), request.password());
        return new AuthTokenResponse(domain.idToken(), domain.expiresInSeconds(), domain.refreshToken());
    }

    @Operation(summary = "Refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "JWT token is refreshed successfully",
                    content = {@Content(schema = @Schema(implementation = AuthToken.class))}),
            @ApiResponse(responseCode = "400", description = "Refresh token should be not empty",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}),
    })
    @PostMapping(path = "refresh", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public AuthTokenResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        ConstraintValidator.validate(request);
        AuthToken domain = tokenCreator.refreshToken(request.refreshToken());
        return new AuthTokenResponse(domain.idToken(), domain.expiresInSeconds(), domain.refreshToken());
    }

    @Operation(summary = "Revoke refresh token")
    @ApiSecurityIsRequired
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh token revoked successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))})
    })
    @PostMapping(value = "revoke")
    @ResponseStatus(HttpStatus.OK)
    public void revokeAccess(@FromAuthToken String userId) throws UserNotFoundException {
        identityService.revokeAccess(userId);
    }
}
