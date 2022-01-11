package com.revature.models.users;

import java.util.ArrayList;

public class UserFactory {

    //This will be a singleton class

    private static UserFactory factory;

    private UserFactory() {
        super();
    }

    public User makeUser(UserRequest u) {
        //String firstName, String lastName, String username, String password
        if (u.userType == "Customer") return new Customer(u.firstName, u.lastName, u.username, u.password);
        else if (u.userType == "Employee") return new Employee(u.firstName, u.lastName, u.username, u.password);
        else return new Admin(u.firstName, u.lastName, u.username, u.password);
    }

    public static UserFactory getFactory() {
        if (factory != null) {
            return factory;
        }
        factory = new UserFactory();
        return factory;
    }
}