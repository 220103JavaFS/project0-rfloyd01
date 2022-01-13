package com.revature;

import com.revature.controllers.*;
import com.revature.models.users.User;
import io.javalin.Javalin;

public class App {

    private static Javalin app;

    public static void main(String[] args) {
        app = Javalin.create();

        configure(new UserController(), new LoginController(), new LogoutController(), new HomepageController());
        app.start();

    }

    public static void configure(Controller... controllers) {
        for (Controller c:controllers) {
            c.addRoutes(app);
        }
    }
}
