package com.revature.controllers;

import com.revature.models.users.Admin;
import com.revature.models.users.Employee;
import com.revature.models.users.User;
import com.revature.models.util.LoginAttempt;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class LoginController extends Controller {

    private UserService userService = UserService.getUserService();

    private Handler loginDisplay = (ctx) -> {
        //I know were not really supposed to have a front end, but, for ease of use
        //this just shows a sample login attempt

        String messageBody = "Existing users for testing purposes, only copy one set of brackets:\n\n" +
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
                "}\n\n";;
        ctx.status(200);
        ctx.result(messageBody); //always send result to Postman
    };

    private Handler login = (ctx) -> {
        //I currently have a script in Postman that will set the Authentication header based on the username
        //retrieved from this function. I'm currently not sure if there's a way to do this without a Postman
        //script

        //first check to see if there's already a user logged in
        if (ctx.req.getSession(false) != null) {
            //if someone is already logged in then they can't access this page without first logging out
            ctx.status(401); //return 401 Unauthorized code
        }
        else {
            //take the LoginAttempt info passed by the user and pull up the matching info from the database
            LoginAttempt loggy = ctx.bodyAsClass(LoginAttempt.class); //store entered username and password into a LoginAttempt class
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

                    ctx.req.getSession(); //add cookie for the logged in user

                    //before storing user information in cookie, downcast to the appropriate type of user
//                    if (loginUser.userType.equals("Admin")) ctx.sessionAttribute("User", (Admin)loginUser);
//                    else if (loginUser.userType.equals("Employee")) ctx.sessionAttribute("User", (Employee)loginUser);
//                    else ctx.sessionAttribute("User", (Employee)loginUser);
                    ctx.sessionAttribute("User", loginUser); //set the current user in the session cookie

                    ctx.status(202); //return 202 Accepted code

                    //For testing purposes, print out basic user information to the log
                    log.info("Login by: " + ctx.sessionAttribute("User").toString());
                }
                else {
                    //the username exists, however, the password was incorrect. Return 400 Bad Request code
                    //log.info("Password was incorrect. Tried to log in with " + loggy.password + " but the actual password is " + loginUser.getUnencryptedPassword());
                    ctx.status(400);
                }
            }
            catch (Exception e) {
                //if the username doesn't match anything currently in the database then we'll get a nullPtrException
                //which is caught here. No other exceptions are expected so just catch a generic Exception object
                ctx.status(404); //Return 404 Not found code
            }
        }

    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/login", loginDisplay);
        app.post("/login", login);
    }
}
