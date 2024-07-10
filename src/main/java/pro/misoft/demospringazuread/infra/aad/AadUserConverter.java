package pro.misoft.demospringazuread.infra.aad;

import com.microsoft.graph.models.PasswordProfile;
import com.microsoft.graph.models.User;
import pro.misoft.demospringazuread.domain.user.PhoneNumber;

import java.util.Objects;

public class AadUserConverter {
    private static final String AAD_USER_TYPE = "Guest";

    private AadUserConverter() {
    }

    public static User toBasicAadUser(PhoneNumber phoneNumberParam, String password, String tenantName) {
        String phoneNumber = phoneNumberParam.e164formatted();
        User user = toAadUserWithNewPassword(password);
        user.userType = AAD_USER_TYPE;
        user.mobilePhone = phoneNumber;
        user.accountEnabled = true;
        user.displayName = phoneNumber;
        user.mailNickname = phoneNumber;
        user.userPrincipalName = userPrincipalName(tenantName, phoneNumberParam);
        return user;
    }

    public static String userPrincipalName(String tenantName, PhoneNumber phoneNumber) {
        var tenantNameNormalised = tenantName.replace("-", "");
        return phoneNumber.e164formatted().replace("+", "") + "@" + tenantNameNormalised + ".onmicrosoft.com";
    }

    public static pro.misoft.demospringazuread.domain.user.User toDomainUser(User aadUser) {
        pro.misoft.demospringazuread.domain.user.User domainUser = new pro.misoft.demospringazuread.domain.user.User();

        domainUser.setFirstName(aadUser.givenName);
        domainUser.setLastName(aadUser.surname);
        domainUser.setCountry(aadUser.country);

        String[] streetParts = Objects.requireNonNull(aadUser.streetAddress).split(",", 2);
        String street = streetParts[0];
        Integer code = streetParts.length > 1 ? Integer.parseInt(streetParts[1]) : 0;
        String city = Objects.requireNonNull(aadUser.city).startsWith("city-") ? aadUser.city.substring(5) : aadUser.city;

        pro.misoft.demospringazuread.domain.user.Address address = new pro.misoft.demospringazuread.domain.user.Address(street, code, city, "");
        domainUser.setAddress(address);

        return domainUser;
    }


    public static User toAadUser(pro.misoft.demospringazuread.domain.user.User domainUser) {
        User user = new User();
        user.givenName = domainUser.getFirstName();
        user.surname = domainUser.getLastName();
        user.displayName = domainUser.getFirstName() + " " + domainUser.getLastName();
        user.country = domainUser.getCountry();
        user.streetAddress = domainUser.getAddress().street() + "," + domainUser.getAddress().extraDetails();
        user.city = "city-" + domainUser.getAddress().city();
        return user;
    }

    public static User toAadUser(PhoneNumber phoneNumberParam, String tenantName) {
        String phoneNumber = phoneNumberParam.e164formatted();
        User user = new User();
        user.mobilePhone = phoneNumber;
        user.mailNickname = phoneNumber;
        user.userPrincipalName = userPrincipalName(tenantName, phoneNumberParam);
        return user;
    }

    public static User toAadUserWithNewPassword(String password) {
        User user = new User();
        user.passwordPolicies = "DisablePasswordExpiration"; //NOSONAR
        PasswordProfile passwordProfile = new PasswordProfile();
        passwordProfile.forceChangePasswordNextSignIn = false;
        passwordProfile.password = password;
        user.passwordProfile = passwordProfile;
        return user;
    }
}
