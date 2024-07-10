package pro.misoft.demospringazuread.infra.web.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pro.misoft.demospringazuread.domain.auth.TokenDecoder;
import pro.misoft.demospringazuread.domain.user.UserNotFoundException;
import pro.misoft.demospringazuread.infra.auth.FromAuthToken;


public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(UserIdArgumentResolver.class);
    private final TokenDecoder tokenDecoder;

    public UserIdArgumentResolver(TokenDecoder tokenDecoder) {
        this.tokenDecoder = tokenDecoder;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameter().getDeclaredAnnotation(FromAuthToken.class) != null;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws UserNotFoundException {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        String userId;
        if (!StringUtils.isBlank(auth)) {
            String jwtToken = auth.replace("Bearer ", "");
            userId = tokenDecoder.getExternalUserIdFromAuthToken(jwtToken);
        } else {
            throw new IllegalArgumentException("HTTP header should be present to identify user");
        }
        log.debug("Auth token parsed to externalUserId={}", userId);
        return userId;
    }
}
