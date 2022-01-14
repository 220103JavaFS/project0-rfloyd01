package com.revature.dao;

import com.revature.models.users.*;
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

        //first add an admin
        users.add(new Admin("Admin", "Robert", "Floyd", "rfloyd01", "Apples2oranges!"));

        //then add an employees
        users.add(new Employee("Employee", "Scott", "Olsen", "Sno19", "Guitar_Man12"));

        //finally add some customers
        users.add(new Customer("Customer", "Jonathan", "Miller", "JJMM07", "123Ab!!powl"));
        users.add(new Customer("Customer", "Daniel", "Preuss", "DanThaMan", "CodingIsKewl420$"));
    }

    public static String getUserTypeDAO(String currentUser) {
        //Takes in the username passed to the function and queries the database to see if the user
        //exists. If so, return the userType of that user (i.e. Customer, Employee, Admin). If the
        //username isn't in the database return a blank string

        //TODO: Function to query database will go here
        //  for now just return a blank string.

        return "";
    }

    public static User getBasicUserInformationDAO(String userName) {

        //TODO: Add database functionality after learning it.

        log.info("The username " + userName + " was passed to the getBasicUserInformationDAO() function.");

        //Scan the database to and return the information associated with the username
        for (User u:users) {
            if (u.username.equals(userName)) {
                log.info("User was found, passing info up from the DAO layer.");
                return u;
            }
        }

        //if no username match was found then return a blank user which will kick up an exception in the Controller layer
        return (User) new Customer(); //this could be a customer, employee or admin, it doesn't matter and will trigger the exceptio regardless.
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
            users.add(newUser); //TODO: ultimately this will need to go into the database and not an ArrayList
            return 0; //if add works then return 0 error code
        }
        catch (Exception e) {
            //I'm not entirely sure what would cause a failure here, but put the creation of a new
            //user into a try/catch to be safe
            return 0b10000000;
        }
    }

    public boolean validUsernameDAO(String username) {
        //this function scans the user database to see if the passed username already exists or not
        log.info("username passed to the DAO layer is: " + username);
        for (User u:users) {
            if (u.username.equals(username)) return false;
        }
        return true; //didn't find the username in the database so the passed username is free to be used.
    }
}
