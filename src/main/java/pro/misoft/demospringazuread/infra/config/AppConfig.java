package pro.misoft.demospringazuread.infra.config;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import pro.misoft.demospringazuread.infra.aad.ActiveDirectoryProperties;

import java.util.List;

@Configuration
@EnableConfigurationProperties({ActiveDirectoryProperties.class})
public class AppConfig {

    @Bean
    public GraphServiceClient graphServiceClient(ActiveDirectoryProperties aadProps) {
        return GraphServiceClient.builder().authenticationProvider(getTokenCredentialAuthProvider(aadProps)).buildClient();
    }

    private static TokenCredentialAuthProvider getTokenCredentialAuthProvider(ActiveDirectoryProperties aadProps) {
        ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(aadProps.getClientId())
                .clientSecret(aadProps.getClientSecret())
                .tenantId(aadProps.getTenantId())
                .build();
        return new TokenCredentialAuthProvider(List.of(aadProps.getGraphScope()), clientSecretCredential);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
