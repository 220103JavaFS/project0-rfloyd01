package com.revature.models.users;

import java.util.Objects;

public class UserRequest {
    //this class serves as a template to be passed to the UserFactory to create actual users
    public String userType;
    public String firstName;
    public String lastName;
    public String username;
    public String password;

    public UserRequest() {
        super();
    }

    public UserRequest(String userType, String firstName, String lastName, String username, String password) {
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRequest that = (UserRequest) o;
        return Objects.equals(userType, that.userType) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userType, firstName, lastName, username, password);
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "userType='" + userType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
