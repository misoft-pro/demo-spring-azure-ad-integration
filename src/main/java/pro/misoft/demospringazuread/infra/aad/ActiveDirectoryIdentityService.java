package pro.misoft.demospringazuread.infra.aad;

import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.misoft.demospringazuread.domain.common.Id;
import pro.misoft.demospringazuread.domain.user.IdentityService;
import pro.misoft.demospringazuread.domain.user.PhoneNumber;

@Retry(name = "aadClient")
@Service
public class ActiveDirectoryIdentityService implements IdentityService {

    private static final Logger log = LoggerFactory.getLogger(ActiveDirectoryIdentityService.class);
    private final String tenantName;
    private final GraphServiceClient graphClient;

    public ActiveDirectoryIdentityService(GraphServiceClient graphServiceClient, ActiveDirectoryProperties aadProps) {
        this.tenantName = aadProps.getTenantName();
        this.graphClient = graphServiceClient;
    }

    @Override
    public Id createUser(PhoneNumber phoneNumberParam, String password) {
        User user = AadUserConverter.toBasicAadUser(phoneNumberParam, password, tenantName);
        User createdUser = graphClient.users().buildRequest().post(user);
        log.info("User with externalId={} was successfully created in ActiveDirectory", createdUser.id);
        return new Id(createdUser.id);
    }

    @Override
    public pro.misoft.demospringazuread.domain.user.User getUser(String userId) {
        User aadUser = graphClient.users(userId).buildRequest().get();
        log.info("User with externalId={} was successfully found in ActiveDirectory", aadUser.id);
        return AadUserConverter.toDomainUser(aadUser);
    }

    @Override
    public void patchUser(pro.misoft.demospringazuread.domain.user.User domainUser) {
        User user = AadUserConverter.toAadUser(domainUser);
        graphClient.users(domainUser.getExternalId()).buildRequest().patch(user);
        log.info("User with id={}, extId={} was successfully patched in ActiveDirectory", domainUser.getId(), domainUser.getExternalId());
    }

    @Override
    public void patchUser(String externalId, PhoneNumber phoneNumber) {
        User user = AadUserConverter.toAadUser(phoneNumber, tenantName);
        graphClient.users(externalId).buildRequest().patch(user);
        log.info("User with externalId={} has phone number successfully changed in ActiveDirectory", externalId);
    }

    @Override
    public void patchPassword(String aadUserId, String password) {
        User user = AadUserConverter.toAadUserWithNewPassword(password);
        graphClient.users(aadUserId).buildRequest().patch(user);
        log.info("Password of user with externalId={} was successfully patched in ActiveDirectory", aadUserId);
    }

    public void revokeAccess(String aadUserId) {
        graphClient.users(aadUserId).revokeSignInSessions().buildRequest().post();
        log.info("User sessions of externalId={} were revoked in ActiveDirectory", aadUserId);
    }
}
