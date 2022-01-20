package com.revature.controllers;

import com.revature.models.users.User;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomepageController extends Controller {

    private UserService userService = UserService.getUserService(); //is it ok to have multiple userService objects?
    private static Logger log = LoggerFactory.getLogger(HomepageController.class); //Do all classes get their own logger?

    private Handler homepageGet = (ctx) -> {
        //I know were not really supposed to have a front end, but, for ease of use
        //this just shows a samples of logins and user creations for the presentation

        String messageBody = "Login Examples:\n" +
                "Admin Example:\n" +
                "{\n" +
                "    \"username\" : \"rfloyd01\",\n" +
                "    \"password\" : \"Coding_is_kewl34\"\n" +
                "}\n\n" +
                "Employee Example:\n" +
                "{\n" +
                "    \"username\" : \"Sno19\",\n" +
                "    \"password\" : \"Guitar_man12\"\n" +
                "}\n\n" +
                "Customer Example:\n" +
                "{\n" +
                "    \"username\" : \"JJMM07\",\n" +
                "    \"password\" : \"EggsInMyDr@nk89\"\n" +
                "}\n\n" +
                "User Creation Example:\n" +
                "{\n" +
                "    \"userType\" : \"Customer\",\n" +
                "    \"firstName\" : \"Miles\",\n" +
                "    \"lastName\" : \"Davis\",\n" +
                "    \"username\" : \"MDavis59\",\n" +
                "    \"password\" : \"iG0tsTheBlu$\"\n" +
                "}\n\n" +
                "Account Creation Example:\n" +
                "{\n" +
                "    \"requestNumber\" : \"1\",\n" +
                "    \"customerName\" : \"JStarita\",\n" +
                "    \"accountType\" : \"Checking\"\n" +
                "}\n\n" +
                "Account Creation Exercise Example:\n" +
                "Used to either accept or reject a request for a new account: \n" +
                "{\n" +
                "    \"requestNumber\" : \"1\",\n" +
                "    \"decision\" : \"Yes\"\n" +
                "}\n\n" +
                "Account Edit Example:\n" +
                "Can either Deposit or Withdraw from an existing account: \n" +
                "{\n" +
                "    \"action\" : \"Deposit\",\n" +
                "    \"amount\" : \"1234.89\"\n" +
                "}\n\n";

        ctx.status(200);
        ctx.result(messageBody); //always send result to Postman
    };

    @Override
    public void addRoutes(Javalin app) {

        app.get("", homepageGet);
    }
}
