package pro.misoft.demospringazuread.infra.auth;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = OpenApiDocConfiguration.BEARER_AUTH,
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(
                title = "User API",
                version = "v1",
                description = "User API for Azure AD"
        ))
@Configuration
@SuppressWarnings("java:S1118")
public class OpenApiDocConfiguration {
    public static final String BEARER_AUTH = "Bearer";

    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(FromAuthToken.class);
    }
}
