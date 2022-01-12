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

        //This function handles some logic when creating a new user, namely, making sure correct information was passed.
        //A binary number will be used to track all errors that occur while tyring to make a user. If there are
        //no issues then this binary number will be a 0

        //There are 7 possible reasons for the creation request to fail at this stage, each bit is responsible for storing one
        //of these errors. Certain errors will be "guilty until proven innocent" (i.e. the password will be assumed to
        //have no lowercase letters until the first lowercase letter is actually seen)
        int errorCode = 0b1111000;

        //First, we have to check if the desired userType is valid (ideally when creating a new user there would be
        //some kind of check box on the front end to make sure you only select a valid userType, however, this app isn't
        //going to have a front end and is only passed a string so this check is necessary)
        switch (u.userType) {
            case "Customer":
            case "Employee":
            case "Admin":
                break; //these are all fine options
            default:
                errorCode |= 0b1;
                //Note: After finding this first error it would be possible to just break out of the user creation
                //method, however, we let this method complete to see if there are any other errors to save the user
                //the hassle of multiple attempts when creating an account
        }

        //Next, we need to access the DAO layer to see if the selected username already exists or not
        if (!validUsernameDAO(u.username)) errorCode |= 0b10;

        //Finally, we need to make sure that the password passed in with the 'u' parameter meets the strict password
        //requirements. These requirements are:
        //1. password must be at least 10 characters in length
        //2. password must feature at least 1 lowercase letter
        //3. password must feature at least 1 uppercase number
        //4. password must have at least 1 number
        //5. password must have at least 1 special character.
        if (u.password.length() < 10) errorCode |= 0b100;

        //look through the password one character at a time and see if each criterion has been met.
        //If each bit is set from a 1 to a 0 in the errorCode then all the criteria have been met
        for (int i = 0; i < u.password.length(); i++) {
            char c = u.password.charAt(i);

            //use the binary and operator to remove errors from error code if necessary
            if (c >= 'A' && c <= 'Z') errorCode &= 0b1110111; //uppercase criteria met
            else if (c >= 'a' && c <= 'z') errorCode &= 0b1101111; //lowercase criteria met
            else if (c >= '0' && c <= '9') errorCode &= 0b1011111; //number criteria met
            else errorCode &= 0b111111; //special character criteria met TODO - may want to consider limiting which characters count here

            if (errorCode == 0) {
                //as soon as our errorCode has been reduced to 0 we are cleared to create a new user so
                //we invoke the UserFactory. If for some reason there's a failure in the DAO layer when
                //calling the user factory it's error code will be added here, indicating that the
                //user wasn't actually created.
                return errorCode | userDAO.addUser(u);
            }
        }

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

    public String getUserTypeService(String currentUser) {
        // a simple request to the DAO layer to return the userType for the given user.
        //It checks to see that the username isn't blank before making the request of the DAO
        //because usernames can't be blank.
        if (currentUser == "") return "";
        else return UserDAO.getUserTypeDAO(currentUser);
    }

}
