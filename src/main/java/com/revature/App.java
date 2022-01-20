package com.revature;

import com.revature.controllers.*;
import com.revature.models.users.User;
import io.javalin.Javalin;

import java.sql.SQLException;

import static com.revature.models.util.ConnectionUtil.getConnection;

public class App {

    private static Javalin app;

    public static void main(String[] args) {
        app = Javalin.create();

        configure(new UserController(), new LoginController(), new LogoutController(), new HomepageController());

//        try {
//            getConnection();
//            System.out.println("Connection successful");
//        } catch (SQLException e) {
//            System.out.println("Conenction failed.");
//            e.printStackTrace();
//        }

        app.start();

    }

    public static void configure(Controller... controllers) {
        for (Controller c:controllers) {
            c.addRoutes(app);
        }
    }
}
