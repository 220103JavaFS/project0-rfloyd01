package com.revature;

import com.revature.controllers.*;
import io.javalin.Javalin;

public class App {

    private static Javalin app;

    public static void main(String[] args) {
        app = Javalin.create();

        configure(new UserController(), new LoginController(), new LogoutController(), new HomepageController(), new AccountController());

        app.start();
    }

    public static void configure(Controller... controllers) {
        for (Controller c:controllers) {
            c.addRoutes(app);
        }
    }
}
