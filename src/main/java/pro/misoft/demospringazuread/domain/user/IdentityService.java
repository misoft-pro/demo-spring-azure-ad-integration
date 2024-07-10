package pro.misoft.demospringazuread.domain.user;


import pro.misoft.demospringazuread.domain.common.Id;

public interface IdentityService {

    Id createUser(PhoneNumber phoneNumber, String password);

    User getUser(String userId);

    void patchUser(User user);

    void patchUser(String externalId, PhoneNumber phoneNumber);

    void patchPassword(String userId, String password);

    void revokeAccess(String userId);
}
