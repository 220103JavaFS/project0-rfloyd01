package com.revature.dao;

import com.revature.models.users.Employee;
import com.revature.models.users.User;

import java.util.ArrayList;

public class UserDAO {

    private static ArrayList<User> users;

    public UserDAO () {
        //Normally this is where would be adding or retreiving things from an actual data base, however, we haven't
        //learned SQL yet so for now just use strings

        users.add(new Employee("Robert", "Floyd"));
        users.add(new Employee("Jonathan", "Miller"));
        users.add(new Employee("Daniel", "Preuss"));
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }
}
