package com.revature.controllers;

import com.revature.models.users.Customer;
import com.revature.models.users.User;
import com.revature.models.util.JSONResponse;
import com.revature.models.util.LoginAttempt;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class LoginController extends Controller {

    private UserService userService = UserService.getUserService(); //is it ok to have multiple userService objects?

    private Handler loginDisplay = (ctx) -> {
        //Display functions will return with ctx.result() instead of ctx.json() for better readability.

        //first check to see if there's already a user logged in, if so, prompt them to logout before logging
        //in
        String currentUser = ctx.header("postmanUsername");
        StringBuilder messageBody = new StringBuilder();

        if (currentUser == "") {
            messageBody.append("Welcome to the Login Page!!\n" +
                    "To login using Postman please pass a command with the following syntax to {POST - localhost/8080/login} :\n" +
                    "{\n" +
                    "    \"username\" : \"{your_username_here}\",\n" +
                    "    \"password\" : \"{your_password_here}\"\n" +
                    "}\n\n" +
                    "Please note that all words must be encased in double quotation marks. Your actual username and password\n" +
                    "will replace the brackets and what's inside of them.");
            ctx.status(200);
        }
        else {
            messageBody.append("Can't login because someone else is already logged in, " +
                    "log out before switching to a new user by visiting {GET - localhost:8080/logout}.");
        }
        ctx.result(messageBody.toString()); //always send result to Postman
    };

    private Handler login = (ctx) -> {
        //TODO: Change the HTTP response status numbers, currently they're all 200

        //I currently have a script in Postman that will set the Authentication header based on the username
        //retrieved from this function. I'm currently not sure if there's a way to do this without a Postman
        //script

        //first check to see if there's already a user logged in, if so, prompt them to logout before logging
        //in
        LoginAttempt loggy = ctx.bodyAsClass(LoginAttempt.class); //store entered username and password into a LoginAttempt class
        String currentUser = ctx.header("postmanUsername");
        JSONResponse res = new JSONResponse(); //the JSONResponse class let's us communicate better with Postman

        //log.info("Value obtained from postmanUsername header is " + currentUser);

        if (currentUser == "") {
            //since no one is currently logged in we are ok to proceed with the below logic
            User loginUser = userService.getBasicUserInformation(loggy.username);

            //TODO: ctx.req.getSession(); //This will return an HttpSession object. If none exists a new one will be created
            //  and a cookie will be added to the response for the client to store. This is an easier way to keep track of currentUser information
            //  which will be easier then using a Postman global variable. Update code with this. If a login with bad credentials occurs then
            //  the session can be ended with ctx.req.getSession().invalidate(); //invalidates any open session. This is also how you logout
            //  under normal conditions. HTTP encrypts the body of a message, so sensitve data should be included in the body and not in a header.
            //  ctx.req.getSession(false) will only return a session object if the client sent a cookie along with the request that matches an
            //  open session

            try {
                //if the username exists in the database then this try block will succeed, however, we need to
                //check that the password matches what's in the database.
                if (loginUser.comparePassword(loggy.password)) {
                    //if the password in the login attempt matches the decrypted password of the found user then
                    //log them into the system
                    //ctx.header("postmanUsername", loggy.username);
                    res.messageBody = "Login attempt was successful, welcome! Please go back to the homepage at " +
                            "{GET - localhost:8080} for a list of the things you can now do.";
                    res.newValue = loggy.username;

                    ctx.status(200);
                }
                else {
                    //the username was correct, however, the password was incorrect. Prompt the user to try again.
                    res.messageBody = "Login attempt failed. The password entered didn't match the password in the database. Please " +
                                    "re-enter the password and try again.";
                    ctx.status(200);
                }
            }
            catch (Exception e) {
                //if the username doesn't match anything currently in the database then we'll get a nullPtrException
                //which is caught here. No other exceptions are expected so just catch a generic Exception object
                res.messageBody = "Login attempt failed. The username entered isn't in the database. Please " +
                        "re-enter the username and try again.";
                ctx.status(200);
            }
        }
        else {
            res.messageBody = "As already mentioned, you are already logged in so you must log out before " +
                    "trying to login to another user account.";
            ctx.status(200);
        }
        ctx.json(res); //always send back the result to postman so we can see what happened.
    };

    private Handler logout = (ctx) -> {
        //I currently have a script in Postman that will set the Authentication header based on the username
        //retrieved from this function. I'm currently not sure if there's a way to do this without a Postman
        //script

        //first check to see if there's already a user logged in, if so, prompt them to logout before logging
        //in
        String currentUser = ctx.header("username");
        if (currentUser == "") {
            //create a pretend user
            log.info("Can't logout because nobody is logged in.");

            //Postman is expecting to get a value here so just return the current value
            ctx.json("{\"username\" : \"" + currentUser + "\"}");
        }
        else {
            ctx.json("{\"username\" : \"\"}");
            ctx.status(200);
            log.info("Successfully logged out.");
        }

    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/login", loginDisplay);
        app.post("/login", login);
    }
}
