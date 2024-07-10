package pro.misoft.demospringazuread.infra.aad;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "azure.activedirectory")
public class ActiveDirectoryProperties {

    private String tenantName;
    private String graphScope;
    private String clientId;
    private String clientSecret;
    private String tenantId;

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getGraphScope() {
        return graphScope;
    }

    public void setGraphScope(String graphScope) {
        this.graphScope = graphScope;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
