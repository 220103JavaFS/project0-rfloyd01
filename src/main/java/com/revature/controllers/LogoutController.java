package com.revature.controllers;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class LogoutController extends Controller {

    private Handler logout = (ctx) -> {

        //we can only access the logout page if we're actually logged in
        if (ctx.req.getSession(false) != null) {
            //log the current user out
            //TODO: Do I need to set the "User" attribute of the cookie to null here by using ctx.consumeSessionAttribute()
            //  or will it automatically go away when the cookie is invalidated?

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
