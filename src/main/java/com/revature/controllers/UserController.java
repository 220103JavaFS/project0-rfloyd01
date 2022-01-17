package com.revature.controllers;

import com.revature.dao.UserDAO;
import com.revature.models.users.Customer;
import com.revature.models.users.User;
import com.revature.models.users.UserRequest;
import com.revature.models.util.JSONResponse;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserController extends Controller {

    //The user controller is what controls things on the user page. Customers should not have access to this page,
    //only Employees and Admins do. Employees view will be restricted so that they can only see their own clients
    //while Admins will be able to see everybody in the database.

    private UserService userService = UserService.getUserService();

    private Handler getUser = (ctx) -> {
        //First we need to make sure that someone is logged in, if not, restrict access to this endpoint
        if (ctx.req.getSession(false) != null) {
            //the info on the current user is stored in the session cookie, just print it out
            ctx.json(ctx.req.getAttribute("User"));
            ctx.status(200); //set status to 200 ok
        }
        else ctx.status(401); //set status to 401 unauthorized
    };

    private Handler getSpecificUser = (ctx) -> {
        //First we need to make sure that someone is logged in, if not, restrict access to this endpoint
        if (ctx.req.getSession(false) != null) {

            //only employees and admins can access this page, if a customer is logged in then restrict access
            User existingUser = (User)ctx.req.getAttribute("User");
            if (existingUser.userType == "Customer") {
                ctx.status(401); //set status to 401 not authorized
            }
            else {
                String userName = ctx.pathParam("user_name");
                User desiredUser = userService.getBasicUserInformation(userName); //

                if (desiredUser != null) {
                    if (existingUser.userType.equals("Admin")) {
                        //admins can view anyone else info so just display the user
                        ctx.json(desiredUser);
                    }
                    else {
                        //the current user logged in is an employee, they can only view their own customers
                        if (desiredUser.userType == "Customer") {
                            Customer castUser = (Customer)desiredUser;
                            if (castUser.getAssignedEmployee().username.equals(existingUser.username)) {
                                //employee has access
                                ctx.json(castUser);
                                ctx.status(200);
                            }
                            else ctx.status(401); //access restricted
                        }
                        else ctx.status(401); //not authorized to view this user
                    }
                }
                else {
                    //the username in the path doesn't exist in the database, set status to 404 not found
                    ctx.status(404);
                }
                ctx.json(ctx.req.getAttribute("User"));
                ctx.status(200); //set status to 200 ok
            }
        }
        else ctx.status(401); //set status to 401 unauthorized
    };

    private Handler createUser = (ctx) -> {

        //TODO: should the below user be created by a user factory? I think so since we won't know the kind of user until runtime
        //TODO: to accomplish this the ctx.body() shouldn't contain a user, but some other class, maybe a
        UserRequest u = ctx.bodyAsClass(UserRequest.class);
        JSONResponse res = new JSONResponse(); //create a JSON response class to get info to postman

        int errorId = userService.newUser(u);
        if (errorId == 0) {
            res.messageBody = "Successfully created new user. You've been automatically logged in." +
                    "Please visit {GET - localhost:8080} to see what you can do with your new" +
                    "account!";
            res.newValue = u.username;

            //If the new user is a customer, they need to be assigned to an employee who wil handle
            //their accounts. This will require a call down to the DAO layer.
            //if (u.userType.equals("Customer")) userService.assignEmployee();

            ctx.status(201); //the user was successfully created added to the database
        }
        else {
            //something went wrong, print the error message associated with the error code
            if (errorId == 0b10000000)
            {
                res.messageBody = "The user was succesfully created, however, an unknown error" +
                        "occured when trying to add them to the database. Please try again later";
                ctx.status(500); //server or database error
            }
            else {
                //there can be multiple error bits so we need to check for each one in an if statement, not an else if

                StringBuilder errorMessage = new StringBuilder("Couldn't create new user:\n");
                if ((errorId & 0b1000000) > 0) {errorMessage.append("Password didn't include a special character, please update password.\n");}
                if ((errorId & 0b100000) > 0) {errorMessage.append("Password didn't include a number, please update password.\n");}
                if ((errorId & 0b10000) > 0) {errorMessage.append("Password didn't include a lowercase letter, please update password.\n");}
                if ((errorId & 0b1000) > 0) {errorMessage.append("Password didn't include an uppercase letter, please update password.\n");}
                if ((errorId & 0b100) > 0) {errorMessage.append("Password wasn't at least 10 characters, please lengthen password.\n");}
                if ((errorId & 0b10) > 0) {errorMessage.append("Username is already taken, please use a different username.\n");}
                if ((errorId & 0b1) > 0) {errorMessage.append("Not a valid user type, please select a valid user type.\n");}

                res.messageBody = errorMessage.toString(); //add error message in the response
                ctx.status(400); //request error
            }
        }
        ctx.json(res);
    };

    private Handler createUserMessage = (ctx) -> {
        //You will only be allowed to create a new user if you aren't currently logged in, as creating a user will
        //automatically log the new user into the app
        String currentUser = ctx.header("postmanUsername");
        StringBuilder resultString = new StringBuilder();
        //log.info("Current user from postman is:" + currentUser);

        if (currentUser == ""){
            resultString.append("Welcome to the User Creation Page!\n" +
                    "To create a new user please pass a command with the following syntax to {POST - localhost/8080/users/create}:\n" +
                    "{\n" +
                    "    \"userType\"  : \"{your_userType_here}\",\n" +
                    "    \"firstName\" : \"{your_firstName_here}\",\n" +
                    "    \"lastName\"  : \"{your_lastName_here}\",\n" +
                    "    \"username\"  : \"{your_username_here}\",\n" +
                    "    \"password\"  : \"{your_password_here}\"\n" +
                    "}\n\n" +
                    "Please note that all words must be encased in double quotation marks. Your actual information will \n" +
                    "replace the brackets and what's inside of them. There are a few rules for successful account creation.\n" +
                    "1. The user type must be either Customer, Employee or Admin. The first letter must be capitalized.\n" +
                    "2. Your username must be unique. If an existing user already has that username you will be prompted to change it.\n" +
                    "3. The password must be at least 10 characters long, contain both an uppercase and lowercase letter, contain " +
                    "a number, and contain a unique character such as !, @ or &\n\n" +
                    "Here's an example of a successful entry:\n" +
                    "{\n" +
                    "    \"userType\"  : \"Customer\",\n" +
                    "    \"firstName\" : \"Robert\",\n" +
                    "    \"lastName\"  : \"Floyd\",\n" +
                    "    \"username\"  : \"rfloyd01\",\n" +
                    "    \"password\"  : \"Coding_is_kewl34\"\n" +
                    "}");
            ctx.status(200);
        }
        else {
            resultString.append("You must logout before creating a new user. To do so please visit " +
                    "{GET - localhost:8080/logout} for more information.");
            ctx.status(200);
        }
        ctx.result(resultString.toString()); //print out the body to Postman
    };

    @Override
    public void addRoutes(Javalin app) {
        //User viewing
        app.get("/users", getUser); //returns info for the user currently logged in
        app.get("/users/{user_name}", getSpecificUser); //returns info for the user specified by "user_name" variable

        //User creation
        app.post("/users", createUser);

        //User editing
        //app.put("/users", editUser);
    }
}
