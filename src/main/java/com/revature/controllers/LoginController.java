package com.revature.controllers;

import com.revature.models.users.Customer;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController extends Controller {

    //private LoginService loginService = new LoginService();
    private static Logger log = LoggerFactory.getLogger(LoginController.class); //Do all classes get their own logger?

    private Handler loginDisplay = (ctx) -> {
        //I currently have a script in Postman that will set the Authentication header based on the username
        //retrieved from this function. I'm currently not sure if there's a way to do this without a Postman
        //script

        //first check to see if there's already a user logged in, if so, prompt them to logout before logging
        //in
        String currentUser = ctx.header("username");
        if (currentUser == "") {
            //create a pretend user
            String resultString = "Welcome to the Login Page!!\n" +
                    "To login using Postman please pass a command with the following syntax to 'POST - localhost\\8080\\login':\n" +
                    "{\n" +
                    "    \"username\" : \"{your_username_here}\",\n" +
                    "    \"password\" : \"{your_password_here}\"\n" +
                    "}\n\n" +
                    "Please note that all words must be encased in double quotation marks. Your actual username and password\n" +
                    "will replace the brackets and what's inside of them.";
            ctx.result(resultString);
            ctx.status(200);
        }
        else {
            log.info("Can't login because someone else is already logged in, " +
                    "log out before switching to a new user.");

            //Postman is expecting to get a value here so just return the current value
            ctx.json("{\"username\" : \"" + currentUser + "\"}");
        }

    };

    private Handler login = (ctx) -> {
        //I currently have a script in Postman that will set the Authentication header based on the username
        //retrieved from this function. I'm currently not sure if there's a way to do this without a Postman
        //script

        //first check to see if there's already a user logged in, if so, prompt them to logout before logging
        //in
        String currentUser = ctx.header("username");
        if (currentUser == "") {
            //create a pretend user
            Customer testCustomer = new Customer("Customer", "Bobby", "Floyd", "rfloyd01", "yeetMyFeet23*&");
            ctx.json(testCustomer);
            ctx.status(200);
        }
        else {
            log.info("Can't login because someone else is already logged in, " +
                    "log out before switching to a new user.");

            //Postman is expecting to get a value here so just return the current value
            ctx.json("{\"username\" : \"" + currentUser + "\"}");
        }

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
