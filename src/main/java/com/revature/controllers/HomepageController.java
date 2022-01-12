package com.revature.controllers;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class HomepageController extends Controller {

    private Handler homepageGet = (ctx) -> {
        //This will display a message when you first open the application and prompt you with the things that
        //you can do.

        String userName = ctx.header("username"); //get the username from the custom HTTP header
        String displayMessage;

        if (userName == "") {
            //no one is logged in, prompt the user to either login or create an account.
            displayMessage = "You aren't currently logged in, either loging by visiting localhost:8080\\login" +
                    "or create an account by visiting localhost:8080\\users\\create";
            ctx.result(displayMessage);
            ctx.status(204);
        }
        else {
            //a user is logged in, we need to make calls to the service layer and then DAO layer to figure out
            //what kind of user is logged in. Depending on the user type we will display different functionality.

            //TODO: I've already created functionality through the UserService that will get the userType for a
            //  given username. Would it be better to recreate this functionality in a HomepageService class or is
            //  it ok to just re-use the UserService function? For now I'm just going to reuse the UserService function
            //  as it isn't dependent on being in the \user path like functions in the UserController class are.
        }
    }
    @Override
    public void addRoutes(Javalin app) {
        app.get("", homepageGet);
    }
}
