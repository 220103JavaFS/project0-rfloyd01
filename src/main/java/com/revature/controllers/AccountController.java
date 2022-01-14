package com.revature.controllers;

import com.revature.models.accounts.Account;
import com.revature.models.users.User;
import com.revature.services.AccountService;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class AccountController extends Controller {

    private AccountService accountService = new AccountService();
    private UserService userService = UserService.getUserService();

    private ArrayList<Account> activeAccounts = new ArrayList<>();

    private Handler getAccountsMessage = (ctx) -> {
        //The main accounts page will display a different view depending on what kind of user you are.
        //If you aren't logged in, it won't show anything interesting and will prompt you to login.
        //If you're a customer it will show you a list of your current accounts.
        //If you're an employee it will show you all of your customers accounts.
        //If you're an admin it will show you the accounts of everyone
        StringBuilder resultString = new StringBuilder("Welcome to the Accounts Page!\n");
        String postmanUser = ctx.header(postmanUsername);
        User currentUser = userService.getBasicUserInformation(postmanUser);

        try {
            switch (currentUser.userType) {
                case "Admin":
                    //activeAccounts = accountService.getAllAccounts();
                    break;
                case "Employee":
                    //activeAccounts = accountService.getEmployeeAccounts(currentUser);
                    break;
                default:
                    //activeAccounts = accountService.getCustomerAccounts(currentUser);
            }

            //show all the accounts retrieved
            ctx.json(activeAccounts);
            ctx.status(200);
        }
        catch (Exception e) {
            //if no one is logged in then we'll get a nullPtrException and this block will be executed
            resultString.append("You aren't currently logged in, to view your accounts please visit {GET - localhost:8080/login} " +
                    "for more information about logging in. After logging in come back to this page to view your accounts.");

            ctx.result(resultString.toString());
            ctx.status(300); //status 300 because you should be redirected to the login page in a real app
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        //User viewing
        app.get("/accounts", getAccountsMessage);
        //app.post("/accounts", getAccounts);

        //User creation
        //app.get("/accounts/{account-number}", createUserMessage);
        //app.post("/accounts/{account-number}", createUser);
    }
}
