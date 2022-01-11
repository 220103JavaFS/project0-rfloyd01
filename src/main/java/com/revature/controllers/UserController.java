package com.revature.controllers;

import com.revature.dao.UserDAO;
import com.revature.models.users.User;
import com.revature.models.users.UserRequest;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserController extends Controller {

    private UserService userService = new UserService();
    private static Logger log = LoggerFactory.getLogger(UserController.class); //Do all classes get their own logger?

    private Handler getAllUsers = (ctx) -> {
        List<User> list = userService.getAllUsers();

        ctx.json(list);
        ctx.status(200);
    };

    //TODO: create a getAllEmployees Handler

    //TODO: create a getAllCustomers Handler

    private Handler createUser = (ctx) -> {

        //TODO: should the below user be created by a user factory? I think so since we won't know the kind of user until runtime
        //TODO: to accomplish this the ctx.body() shouldn't contain a user, but some other class, maybe a
        UserRequest u = ctx.bodyAsClass(UserRequest.class);
        int errorId = userService.newUser(u);
        if (errorId == 0) {
            ctx.result("Successfully created new user.");
            ctx.status(201); //the user was succesfully created added to the database
        }
        else {
            //something went wrong, print the error message associated with the error code
            if (errorId == 0b10000000)
            {
                ctx.result("The user was succesfully created, however, an unknown error" +
                        "occured when trying to add them to the database. Please try again later");
                ctx.status(500); //server or database error
            }
            else {
                //there can be multiple error bits so we need to check for each one in an if statement

                StringBuilder errorMessage = new StringBuilder();
                if ((errorId & 0b1000000) > 0) {errorMessage.append("Password didn't include a special character, please update password.\n");}
                if ((errorId & 0b100000) > 0) {errorMessage.append("Password didn't include a number, please update password.\n");}
                if ((errorId & 0b10000) > 0) {errorMessage.append("Password didn't include a lowercase letter, please update password.\n");}
                if ((errorId & 0b1000) > 0) {errorMessage.append("Password didn't include an uppercase letter, please update password.\n");}
                if ((errorId & 0b100) > 0) {errorMessage.append("Password wasn't at least 10 characters, please lengthen password.\n");}
                if ((errorId & 0b10) > 0) {errorMessage.append("Username is already taken, please use a different username.\n");}
                if ((errorId & 0b1) > 0) {errorMessage.append("Not a valid user type, please select a valid user type.\n");}

                ctx.result("Couldn't create new user:\n" + errorMessage); //add error message in the response
                ctx.status(400); //request error
            }
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/users", getAllUsers);
        app.post("/users", createUser);
    }
}
