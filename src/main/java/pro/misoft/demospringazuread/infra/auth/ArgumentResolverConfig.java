package pro.misoft.demospringazuread.infra.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pro.misoft.demospringazuread.domain.auth.TokenDecoder;
import pro.misoft.demospringazuread.infra.web.auth.UserIdArgumentResolver;

import java.util.List;

@Component
class ArgumentResolverConfig implements WebMvcConfigurer {

    private final TokenDecoder tokenDecoder;

    ArgumentResolverConfig(TokenDecoder tokenDecoder) {
        this.tokenDecoder = tokenDecoder;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserIdArgumentResolver(tokenDecoder));
    }
}