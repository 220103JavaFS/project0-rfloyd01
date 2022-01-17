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

    private Handler logout = (ctx) -> {

        //we can only access the logout page if we're actually logged in
        if (ctx.req.getSession(false) != null) {
            //log the current user out
            ctx.req.getSession().invalidate();
            ctx.status(202); //return 202 Accepted code
        }
        else ctx.status(401); //no one is logged in so we can't access the logout page
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/logout", logout);
    }
}
