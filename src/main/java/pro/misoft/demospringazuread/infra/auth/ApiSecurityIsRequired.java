package pro.misoft.demospringazuread.infra.auth;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static pro.misoft.demospringazuread.infra.auth.OpenApiDocConfiguration.BEARER_AUTH;

@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@SecurityRequirement(name = BEARER_AUTH)
public @interface ApiSecurityIsRequired {
}
