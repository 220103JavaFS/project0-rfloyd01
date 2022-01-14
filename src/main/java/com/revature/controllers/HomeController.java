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

    @Override
    public void addRoutes(Javalin app) {
        app.get("/home", getHomes);
    }
    //This is a test class from the days lectures
}
