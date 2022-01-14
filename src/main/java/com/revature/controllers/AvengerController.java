package com.revature.controllers;

import com.revature.models.Avenger;
import com.revature.services.AvengerService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AvengerController extends Controller {

    private AvengerService avengerService = new AvengerService();

    private Handler getAvenger = (ctx) -> {

//        if (ctx.req.getSession(false) != null) {
//            String idString = ctx.pathParam("id");
//            int id = Integer.parseInt(idString);
//            Avenger avenger = avengerService.callAvenger(id);
//
//            ctx.json(avenger);
//            ctx.status(200);
//        }
//        else {
//            ctx.status(401);
//        }
        String idString = ctx.pathParam("id");
        int id = Integer.parseInt(idString);
        Avenger avenger = avengerService.callAvenger(id);

        ctx.json(avenger);
        ctx.status(200);

    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/avenger/{id}", getAvenger);
    }
}
