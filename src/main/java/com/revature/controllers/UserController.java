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
    private static Logger log = LoggerFactory.getLogger(UserController.class); //Do all classes get their own logger?

    private Handler getUsersMessage = (ctx) -> {
        String resultString = "Welcome to the User View Page!\n" +
                "To login using Postman please pass a command with the following syntax to 'POST - localhost\\8080\\login':\n" +
                "{\n" +
                "    \"username\" : \"{your_username_here}\",\n" +
                "    \"password\" : \"{your_password_here}\"\n" +
                "}\n\n" +
                "Please note that all words must be encased in double quotation marks. Your actual username and password\n" +
                "will replace the brackets and what's inside of them.";
        ctx.result(resultString);
        ctx.status(200);
    };

    private Handler getUsers = (ctx) -> {

        //First we need to take a look at the Authorization header of the incoming HTTP request to see if the user has
        //access to this page, and if so, what users can they actually see?
        String currentUser = ctx.header("postmanUsername");

        //Need to drill down to the service layer and then further down to the DAO layer to figure out the user type
        String userType = userService.getUserTypeService(currentUser);
        ArrayList<User> userList;
        switch (userType) {
            case "Employee":
                //userList = userService.getCustomers(currentUser);
                break;
            case "Admin":
                userList = userService.getAllUsers();
                break;
            default:
                ctx.result("You don't have access to this page.");
                ctx.status(401);
                return; //TODO: can you tell a handler function to return? it has no return type

        }

        //ctx.json(userList);
        ctx.status(200);

    };

    //TODO: create a getAllEmployees Handler

    //TODO: create a getAllCustomers Handler

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
        app.get("/users", getUsersMessage);
        app.post("/users", getUsers);

        //User creation
        app.get("/users/create", createUserMessage);
        app.post("/users/create", createUser);

        //TODO: Should logging in and out be it's own controller? I'm still a little confused about what exactly should
        //  get a controller. In my mind, each model class (or group of model classes) would have a separate controller
        //  that's capable of handling many different functions. Or maybe it's more like anything that would have a
        //  separate endpoint (i.e. /users and /login) would need different controllers? Should ask Tim for clarification.

    }
}
