package com.revature.controllers;

import com.revature.models.users.User;
import com.revature.models.util.JSONResponse;
import com.revature.models.util.LoginAttempt;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutController extends Controller {

    //TODO: currently all HTTP status codes are set to 200, update these at some point
    private UserService userService = new UserService(); //is it ok to have multiple userService objects?

    private Handler logoutDisplay = (ctx) -> {

        //first check to see if there's a user logged in, if so, prompt them to logout
        JSONResponse res = new JSONResponse(); //the JSONResponse class let's us communicate better with Postman
        String currentUser = ctx.header("postmanUsername"); //get the current user from HTTP header

        if (currentUser == "") {
            res.messageBody = "Can't logout because you aren't actually already logged in. For information on how to login" +
                    "please go to {GET - localhost:8080/login}.";
            ctx.status(200);
        }
        else {
            res.messageBody = "Welcome to the Logout Page!!\n" +
                    "To logout please visit {POST - localhost:8080/logout}. No actual commands will need to" +
                    "be issued here, just visiting the link will be sufficient to logout.";
            ctx.status(200);
        }
        ctx.json(res); //always send result to Postman
    };

    private Handler logout = (ctx) -> {
        String currentUser = ctx.header("postmanUsername");
        JSONResponse res = new JSONResponse(); //the JSONResponse class let's us communicate better with Postman
        if (currentUser == "") {
            res.messageBody = "As already mentioned, you aren't logged in so you can't logout before " +
                    "trying to logout, please go to {GET - localhost:8080/login} for information on logging in.";
            ctx.status(200);
        }
        else {
            res.messageBody = "Logout was successful, please return to the homepage at {GET - localhost:8080} " +
                    "for a list of things you can do.";
            res.newValue = ""; //this will change the current username to "nobody" which Postman will recognize as no one being logged in
            ctx.status(200);
        }
        ctx.json(res); //always send result to Postman
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/logout", logoutDisplay);
        app.post("/logout", logout);
    }
}
