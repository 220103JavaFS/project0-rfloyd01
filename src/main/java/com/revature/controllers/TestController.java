package com.revature.controllers;

import com.revature.models.users.Customer;
import com.revature.models.users.Employee;
import com.revature.models.users.User;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class TestController extends Controller {

    Handler test = (ctx) -> {
        Customer customer = new Customer("Admin", "Robert", "Floyd", "rfloyd01", "Haw4infect@");
        Employee employee = new Employee("Admin", "Robert", "Floyd", "rfloyd01", "Haw4infect@");

        ArrayList<User> muh_list = new ArrayList<>();
        muh_list.add(customer);
        muh_list.add(employee);

        ctx.json(employee);
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/test", test);
    }
}
