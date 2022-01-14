package com.revature.controllers;

import com.revature.models.Home;
import com.revature.services.HomeService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class HomeController extends Controller {

    private HomeService homeService = new HomeService();

    private Handler getHomes = (ctx) -> {
        ctx.json(homeService.findAllHomes());
        ctx.status(200);
    };

    private Handler getHome = (ctx) -> {
        //first add the login logic
        if (ctx.req.getSession(false) != null) {
            String homeName = ctx.pathParam("home_name");
            Home home = homeService.findHome(homeName);
            ctx.json(home);
            ctx.status(200);
        }
        else {
            ctx.status(401);
        }

    };

    private Handler updateHome = (ctx) -> {
        //first add the login logic
        if (ctx.req.getSession(false) != null) {
            Home home = ctx.bodyAsClass(Home.class);
            if (homeService.updateHome(home)) {
                ctx.status(202); //status code for successful update
            }
            else {
                ctx.status(400);
            }
        }
        else {
            ctx.status(401);
        }
    };

    private Handler newHome = (ctx) -> {
        //first add the login logic
        if (ctx.req.getSession(false) != null) {
            Home home = ctx.bodyAsClass(Home.class);
            if (homeService.addHome(home)) {
                ctx.status(201); //status code for successful creation
            }
            else {
                ctx.status(400);
            }
        }
        else {
            ctx.status(401);
        }
    };

    @Override
    public void addRoutes(Javalin app) {

        app.get("/home", getHomes);
        app.get("/home/{home_name}", getHomes); //curly braces show that home_name is a path parameter

        app.put("/home", updateHome); //we use put because we're updating an object
        app.post("/home", newHome); //we use post because we're adding a new object
    }
    //This is a test class from the days lectures
}
