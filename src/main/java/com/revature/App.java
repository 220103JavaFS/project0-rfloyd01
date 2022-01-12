package com.revature;

import com.revature.controllers.Controller;
import com.revature.controllers.LoginController;
import com.revature.controllers.UserController;
import com.revature.models.users.User;
import io.javalin.Javalin;

public class App {

    private static Javalin app;

    public static void main(String[] args) {
        app = Javalin.create();

        configure(new UserController(), new LoginController());
        app.start();

    }

    public static void configure(Controller... controllers) {
        for (Controller c:controllers) {
            c.addRoutes(app);
        }
    }
}
