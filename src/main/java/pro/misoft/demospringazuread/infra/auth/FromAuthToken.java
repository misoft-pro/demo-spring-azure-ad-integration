package pro.misoft.demospringazuread.infra.auth;

import java.lang.annotation.*;

/**
 * Annotation indicating a method parameter should be bound to the userId value in authorization token header of http request.
 * <p>Supported for annotated handler methods.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FromAuthToken {
}
