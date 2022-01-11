package com.revature.dao;

import com.revature.models.users.Employee;
import com.revature.models.users.User;
import com.revature.models.users.UserFactory;
import com.revature.models.users.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.util.ArrayList;

public class UserDAO {

    private static ArrayList<User> users;
    private static Logger log = LoggerFactory.getLogger(UserDAO.class); //Do all classes get their own logger?

    public UserDAO () {
        //Normally this is where would be adding or retreiving things from an actual data base, however, we haven't
        //learned SQL yet so for now just use strings

        users = new ArrayList<User>();

        users.add(new Employee("Robert", "Floyd", "rfloyd01", "Apples2oranges!"));
        users.add(new Employee("Jonathan", "Miller", "JJMM07", "123Ab!!powl"));
        users.add(new Employee("Daniel", "Preuss", "DanThaMan", "CodingIsKewl420$"));
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public int addUser(UserRequest u) {
        try {
            //first, invoke the UserFactory to create a user with the given information.
            //the information should all be valid to have reached this method so this is
            //safe

            UserFactory uf = UserFactory.getFactory();
            User newUser = uf.makeUser(u);
            users.add(newUser); //TODO: ultimately this will need to go into the database
            return 0; //if add works then return 0 error code
        }
        catch (Exception e) {
            //I'm not entirely sure what would cause a failure here, but put the creation of a new
            //user into a try/catch to be safe
            return 0b10000000;
        }
    }
}
