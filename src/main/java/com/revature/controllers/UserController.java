package com.revature.controllers;

import com.revature.models.users.Customer;
import com.revature.models.users.Employee;
import com.revature.models.users.User;
import com.revature.models.users.UserRequest;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class UserController extends Controller {

    //The user controller is what controls things on the user page. Customers should not have access to this page,
    //only Employees and Admins do. Employees view will be restricted so that they can only see their own clients
    //while Admins will be able to see everybody in the database.

    //FIELDS
    private UserService userService = UserService.getUserService();

    //METHODS
    //User Viewing Methods
    private Handler getUsers = (ctx) -> {
        //First we need to make sure that someone is logged in, if not, restrict access to this endpoint
        if (ctx.req.getSession(false) != null) {
            //Obtain the User information stored in the cookie and cast it to the appropriate user type
            User currentUser = ctx.sessionAttribute("User");
            log.info("UserController getUsers() handler was called");

            if (currentUser.userType.equals("Admin")) {
                //when admins call this function, by default they see everyone in the database
                ArrayList<User> users = userService.getAllUsersService();
                ctx.json(users);
                ctx.status(200);
            }
            else if (currentUser.userType.equals("Employee")) {
                //Employees can see themselves and all of their customers
                Employee emp = userService.getEmployeeService(currentUser.username);
                ctx.json(emp);
                ctx.status(200);
            }
            else {
                Customer cust = userService.getCustomerService(currentUser.username);
                ctx.json(cust);
                ctx.status(200);
            }
        }
        else ctx.status(401); //set status to 401 unauthorized
    };
    private Handler getSpecificUser = (ctx) -> {
        //First we need to make sure that someone is logged in, if not, restrict access to this endpoint
        if (ctx.req.getSession(false) != null) {

            //only employees and admins can access this page, if a customer is logged in then restrict access
            User existingUser = (User)ctx.sessionAttribute("User");

            //TODO: These nested IF statements are sore on the eyes, rethink some of the logic here and try to utilize a
            //  switch statement somehow.

            if (existingUser.userType.equals("Customer")) {
                ctx.status(401); //set status to 401 not authorized
            }
            else {
                String userName = ctx.pathParam("user_name");
                log.info("Passed username is: " + userName);

                User desiredUser = userService.getBasicUserInformation(userName);
                if (desiredUser == null) ctx.status(404);
                else {
                    String userType = desiredUser.userType; //different usertypes will be displayed differently

                    //log.info("Username exists, found User: " + desiredUser.toString());
                    if (existingUser.userType.equals("Admin")) {
                        //admins can view anyone else info so just display the user
                        switch(userType) {
                            case "Employee":
                                Employee emp = userService.getEmployeeService(userName);
                                ctx.json(emp);
                                break;
                            case "Customer":
                                Customer cust = userService.getCustomerService(userName);
                                ctx.json(cust);
                                break;
                            default:
                                ctx.json(desiredUser); //admins only have basic info so no need to get more specific
                                break;
                        }
                        ctx.status(200);
                    }
                    else {
                        //the current user logged in is an employee, they can only view themselves of their own customers
                        if (desiredUser.userType.equals("Customer")) {
                            Customer cust = userService.getCustomerService(userName);

                            //log.info("Current user is: " + existingUser.username + ", necessary user is: " + cust.getAssignedEmployee());

                            //TODO:The below is going to acquire a look into the DB to see who the assigned employee is. Uncomment below at some point
                            if (cust.getAssignedEmployee().equals(existingUser.username)) {
                                //employee has access
                                ctx.json(cust);
                                ctx.status(200);
                            }
                            else ctx.status(401); //access restricted
                        }
                        else if (desiredUser.username.equals(existingUser.username)) {
                            //it's redundant but employees are allowed to look up themselves using this endpoint
                            ctx.json(existingUser);
                            ctx.status(200);
                        }
                        else ctx.status(401); //not authorized to view this user
                    }
                }
            }
        }
        else ctx.status(401); //set status to 401 unauthorized
    };

    //User Creation Methods
    private Handler createUser = (ctx) -> {
        //we can only create a new user if we aren't currently logged in

        if (ctx.req.getSession(false) != null) {
            ctx.status(401);
        }
        else {
            UserRequest u = ctx.bodyAsClass(UserRequest.class);

            int errorId = userService.createNewUserService(u); //use error codes for debugging purposes
            if (errorId == 0) {
                ctx.status(201); //the user was successfully created added to the database
            }
            else {
                //something went wrong, print all error messages to the info log
                if (errorId == 0b10000000)
                {
                    log.info("The user was succesfully created, however, an unknown error" +
                            "occured when trying to add them to the database. Please try again later");
                    ctx.status(500); //server or database error
                }
                else {
                    //there can be multiple error bits so we need to check for each one in an if statement, not an else if

                    StringBuilder errorMessage = new StringBuilder("Couldn't create new user:\n");
                    if ((errorId & 0b1000000) > 0) {errorMessage.append("Password either didn't include a special character, or had an out of range special character,\nAll characters entered must have an ASCII value between 48 and 126 please update password.\n");}
                    if ((errorId & 0b100000) > 0) {errorMessage.append("Password didn't include a number, please update password.\n");}
                    if ((errorId & 0b10000) > 0) {errorMessage.append("Password didn't include a lowercase letter, please update password.\n");}
                    if ((errorId & 0b1000) > 0) {errorMessage.append("Password didn't include an uppercase letter, please update password.\n");}
                    if ((errorId & 0b100) > 0) {errorMessage.append("Password wasn't at least 10 characters, please lengthen password.\n");}
                    if ((errorId & 0b10) > 0) {errorMessage.append("Username is already taken, please use a different username.\n");}
                    if ((errorId & 0b1) > 0) {errorMessage.append("Not a valid user type, please select a valid user type.\n");}

                    log.info(errorMessage.toString()); //add error message in the response
                    ctx.status(400); //request error
                }
            }
        }
    };

    //User Modifying Methods
    private Handler editUser = (ctx) -> {
        //Allows a User to edit the basic information about themselves. Specifically they can edit their first name, last name, username
        //and password. Other things like adding accounts or reassigning customers for employees will happen elsewhere.

        if (ctx.req.getSession(false) != null) {
            User currentUser = ctx.sessionAttribute("User");
            UserRequest modifiedUser = ctx.bodyAsClass(UserRequest.class);

            if (userService.updateUser(modifiedUser, currentUser.username)) {
                //if the user updates their own info, this should be reflected in the current cookie
                ctx.sessionAttribute("User", userService.getBasicUserInformation(modifiedUser.username));
                ctx.status(202); //the changes were successful
            }
            else ctx.status(400); //something was wrong with the credentials entered
        }
        else {
            ctx.status(401); //must be logged in to edit your own information
        }
    };
    private Handler editSpecificUser = (ctx) -> {
        //Similar to the editUser handler, however, this variant is for Admin use and allows them to edit the basic user
        //information for any user in the system. Technically this Handler can be accessed by both Customers and Employees,
        //however, it will only work if they try and edit their own information (which is redundant because they can do the
        //same with the editUser handler)

        if (ctx.req.getSession(false) != null) {
            User currentUser = ctx.sessionAttribute("User");

            UserRequest modifiedUser = ctx.bodyAsClass(UserRequest.class);
            String pathUsername = ctx.pathParam("user_name");

            if (!currentUser.userType.equals("Admin")) {
                //Employees and Customers can only change their own information, we need to check and see
                //that if the current user is a non-Admin type
                if (!currentUser.username.equals(pathUsername)) {
                    log.info("Current user is: " + currentUser.username + ", Trying to update user: " + pathUsername);
                    ctx.status(401); //these users can only update their own information
                    return; //TODO: Can handlers have returns? Test this
                }
            }

            if (userService.updateUser(modifiedUser, pathUsername)) ctx.status(202); //the changes were successful
            else ctx.status(400); //something was wrong with the credentials entered

        }
        else {
            ctx.status(401); //must be logged in to view this
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        //User viewing
        app.get("/users", getUsers); //returns info for the user currently logged in
        app.get("/users/{user_name}", getSpecificUser); //returns info for the user specified by "user_name" variable

        //User creation
        app.post("/users", createUser);

        //User editing
        app.put("/users", editUser);
        app.put("/users/{user_name}", editSpecificUser);
    }
}
