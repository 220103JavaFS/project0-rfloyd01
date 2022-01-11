package com.revature.models.users;

public class UserRequest {
    //this class serves as a template to be passed to the UserFactory to create actual users
    public String userType;
    public String firstName;
    public String lastName;
    public String username;
    public String password;

    //there's no default constructor here, only one that fills all fields with strings

    public UserRequest(String userType, String firstName, String lastName, String username, String password) {
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
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
