package com.revature.dao;

import com.revature.models.users.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class UserDAO {

    private static ArrayList<User> users;
    private static Logger log = LoggerFactory.getLogger(UserDAO.class); //Do all classes get their own logger?

    public UserDAO () {
        //Normally this is where would be adding or retrieving things from an actual database, however, we haven't
        //learned SQL yet so for now just use strings

        users = new ArrayList<User>();

        //first add an admin
        Admin Bobby = new Admin("Admin", "Robert", "Floyd", "rfloyd01", "Apples2oranges!");
        users.add(Bobby);

        //then add an employees
        Employee Scott = new Employee("Employee", "Scott", "Olsen", "Sno19", "Guitar_Man12");
        Employee Billy = new Employee("Employee", "Billy", "Floyd", "Baggins24", "yeetMyFeet19!");

        //finally add some customers
        Customer Jonathan = new Customer("Customer", "Jonathan", "Miller", "JJMM07", "123Ab!!powl");
        Customer Dan = new Customer("Customer", "Daniel", "Preuss", "DanThaMan", "CodingIsKewl420$");
        //Jonathan.setAssignedEmployee(Scott);
        //Dan.setAssignedEmployee(Billy);
        users.add(Jonathan);
        users.add(Dan);

        //assign customers to employees with an Admin then add them to database
        Bobby.assignCustomer(Scott, Jonathan);
        Bobby.assignCustomer(Billy, Dan);
        users.add(Scott);
        users.add(Billy);
    }

    public static String getUserTypeDAO(String currentUser) {
        //Takes in the username passed to the function and queries the database to see if the user
        //exists. If so, return the userType of that user (i.e. Customer, Employee, Admin). If the
        //username isn't in the database return a blank string

        //Scan the database to and return the information associated with the username
        for (User u:users) {
            if (u.username.equals(currentUser)) {
                return u.userType;
            }
        }

        //if no username match was found then return null
        return null;
    }

    public static User getBasicUserInformationDAO(String userName) {

        //TODO: Add database functionality after learning it.

        //log.info("The username " + userName + " was passed to the getBasicUserInformationDAO() function.");

        //Scan the database to and return the information associated with the username
        for (User u:users) {
            if (u.username.equals(userName)) {
                log.info("User was found, passing info up from the DAO layer.");
                return u;
            }
        }

        //if no username match was found then return null
        return null;
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
        //log.info("username passed to the DAO layer is: " + username);
        for (User u:users) {
            if (u.username.equals(username)) return false;
        }
        return true; //didn't find the username in the database so the passed username is free to be used.
    }

    public void updateUser(UserRequest newInformation, String existingUsername) {
        //iterate through the users until we find the one who needs to have their info updated. Searching by the
        //username in the UserRequest won't work because it's possible the name isn't in the database yet. For this
        //reason we also need to pass the existing username, so we can actually find the entry
        log.info("updateUserDAO() function called.");
        Iterator<User> iter = users.iterator();

        while (iter.hasNext()) {
            User u = iter.next();
            if (u.username.equals(existingUsername)) {
                //update the first name, last name, username and password for the user
                u.username = newInformation.username;
                u.firstName = newInformation.firstName;
                u.lastName = newInformation.lastName;
                u.password = newInformation.password;
            }
        }

    }
}
