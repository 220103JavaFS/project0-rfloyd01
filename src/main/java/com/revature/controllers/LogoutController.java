package com.revature.controllers;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class LogoutController extends Controller {

    //GET HANDLERS
    private Handler logout = (ctx) -> {

        //we can only access the logout page if we're actually logged in
        if (ctx.req.getSession(false) != null) {
            //log the current user out

            ctx.req.getSession().invalidate();
            ctx.status(202); //return 202 Accepted code
        }
        else ctx.status(401); //no one is logged in, restrict access the logout page
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/logout", logout);
    }
}
