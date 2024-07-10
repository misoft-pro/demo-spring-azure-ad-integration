package pro.misoft.demospringazuread.domain.user;

import pro.misoft.demospringazuread.domain.common.Id;

public class User {

    private String firstName;
    private String lastName;
    private String country;
    private Address address;
    private String externalId;
    private Id id;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }
}
