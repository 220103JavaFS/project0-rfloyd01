package com.revature.services;

import com.revature.dao.UserDAO;
import com.revature.models.users.User;
import com.revature.models.users.UserRequest;

import java.util.ArrayList;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public ArrayList<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public int newUser(UserRequest u) {

        //we need to implement logic here to make sure that the information passed to create the new user works.
        //A binary number will be used to track all errors that occured while tyring to make a user. If there are
        //no issues then this binary number will be a 0

        //There are 7 possible reasons for the creation request to fail, each bit is responsible for storing one
        //of these errors. The password errors will be "guilty until proven innocent"
        int errorCode = 0b1111000; //anything wrong with the user creation attempt will be stored in this error code

        //First, we have to check if the desired userType is valid (ideally when creating a new user there would be
        //some kind of check box on the front end to make sure you only select a valid user, however, this app isn't
        //going to have a front end and is only passed a string so this check needs to happen)
        switch (u.userType) {
            case "Customer":
                break; //this is fine
            case "Employee":
                break; //this is fine
            case "Admin":
                break; //this is fine
            default:
                errorCode |= 0b1;
        }

        //Second, we need to access the DAO layer and see if the desired username passed in through 'u' is available
        //(i.e. doesn't already exist in the database)
        if (!validUsernameDAO(u.username)) errorCode |= 0b10;

        //Next, we need to make sure that the password passed in with the 'u' parameter meets all of the strict
        //requirements. These requirements are:
        //1. password must be at least 10 characters in length
        //2. password must feature at least 1 lowercase letter
        //3. password must feature at least 1 uppercase number
        //4. password must have at least 1 number
        //5. password must have at least 1 special character.
        if (u.password.length() < 10) errorCode |= 0b100;

        //look through the password one letter at a time and see if each criterion has been met. Track
        //the criteria using a 4 bit number. If each bit is set from a 0 to a 1 then all the criteria
        //have been met

        for (int i = 0; i < u.password.length(); i++) {
            char c = u.password.charAt(i);

            //use the binary and operator to remove errors from error code if necessary
            if (c >= 'A' && c <= 'Z') errorCode &= 0b1110111; //uppercase criteria met
            else if (c >= 'a' && c <= 'z') errorCode &= 0b1101111; //lowercase criteria met
            else if (c >= '0' && c <= '9') errorCode &= 0b1011111; //number criteria met
            else errorCode &= 0b111111; //special character criteria met TODO - may want to consider limiting which characters count here

            if (errorCode == 0) {
                //as soon as our errorCode has been reduced to 0 we are cleared to create a new user
                //we invoke the UserFactory. If for something there's a failure in the DAO layer when
                //calling the user factory it's error code will be added here, indicating that the
                //user wasn't actually created.
                return errorCode | userDAO.addUser(u);
            }
        }

        System.out.println("Error code is " + errorCode);
        //if we get to this point in the loop then something was wrong with the UserRequest, return the full error code
        //without creating a user
        return errorCode;
    }

    private boolean validUsernameDAO(String username) {
        //this function scans the database to see if the existing username already exists or not.
        //if the name already exists it returns false, otherwise, it returns true
        //TODO: I currently don't have database functionality, return here after I do. For now just always return true
        return true;
    }
}
