package com.revature.controllers;

import com.revature.models.users.User;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomepageController extends Controller {

    private UserService userService = new UserService(); //is it ok to have multiple userService objects?
    private static Logger log = LoggerFactory.getLogger(HomepageController.class); //Do all classes get their own logger?

    private Handler homepageGet = (ctx) -> {
        //This will display a message when you first open the application and prompt you with the things that
        //you can do.

        StringBuilder displayMessage = new StringBuilder("");
        String userName = ctx.header("postmanUsername"); //get the username from the custom HTTP header
        User user = userService.getBasicUserInformation(userName); //returns basic info about the user, such as real name and userType

        try {
            //log.info("The try block in the Hompage GET handler was invoked.");
            //If a user is logged in this try block will execute without an issue.

            //TODO: I've already created functionality through the UserService that will get the userType for a
            //  given username. Would it be better to recreate this functionality in a HomepageService class or is
            //  it ok to just re-use the UserService function? For now I'm just going to reuse the UserService function
            //  as it isn't dependent on being in the user path like functions in the UserController class are.

            //Welcome the user and then show possible commands based on their userType
            displayMessage.append("Welcome, " + user.userType + " " + user.firstName + " " + user.lastName + "! What would you " +
                    "like to do today?\n\n");
            switch (user.userType) {
                case "Admin":
                    displayMessage.append("Look at all users by going to {GET - localhost:8080/users}\n");
                    displayMessage.append("Delete users by going to {GET - localhost:8080/users/delete}\n");
                    displayMessage.append("Approve any open account requests by going to {GET - localhost:8080/accounts/open_requests}\n");
                    break;
                case "Employee":
                    displayMessage.append("Look at all of your customers by going to {GET - localhost:8080/users}\n");
                    displayMessage.append("Approve any open account requests from your customers by going to {GET - localhost:8080/accounts/open_requests}\n");
                    break;
                default:
                    displayMessage.append("Look at your information by going to {GET - localhost:8080/users}\n");
                    displayMessage.append("Look at all of your open accounts by going to {GET - localhost:8080/accounts}\n");
                    displayMessage.append("Look at a specific account by going to {GET - localhost:8080/accounts/{account_number}}\n");
            }

            displayMessage.append("Logout by going to {POST - localhost:8080/logout}"); //all users can logout so put this at the end
            ctx.result(displayMessage.toString());
            ctx.status(200);
        }
        catch (Exception e) {
            //if nobody is currently logged in then we will get a nullPtrException when trying to acces specific variables
            //of a user. Since no one is logged in, don't actually stop the program from running, just
            //prompt the user to either login or create an account.

            displayMessage.setLength(0); //reset anything placed in the stringBuilder from the try block
            log.info("The catch block in the Hompage GET handler was invoked.");

            displayMessage.append("You aren't currently logged in, either login by visiting {GET - localhost:8080/login} " +
                    "or create an account by visiting {GET - localhost:8080/users/create}");

            //log.info("The following message should display in POSTMAN " + displayMessage.toString());
            String finalMessage = displayMessage.toString();
            ctx.result(finalMessage);
            ctx.status(200);
        }
    };

    @Override
    public void addRoutes(Javalin app) {

        app.get("", homepageGet);
    }
}
