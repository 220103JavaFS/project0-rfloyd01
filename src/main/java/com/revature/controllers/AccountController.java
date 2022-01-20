package com.revature.controllers;

import com.revature.models.accounts.Account;
import com.revature.models.accounts.ExerciseAccountRequest;
import com.revature.models.accounts.NewAccountRequest;
import com.revature.models.users.Employee;
import com.revature.models.users.User;
import com.revature.services.AccountService;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class AccountController extends Controller {

    private AccountService accountService = new AccountService();
    private UserService userService = UserService.getUserService();

    private Handler getAccounts = (ctx) -> {
        //If a customer envokes this function, they will be able to see all of their accounts.
        //If an employee evokes this function, they will be able to see all accounts owned by their customers
        //Admins can see all existing accounts when calling this function

        //first check to see if anyone is logged in
        if (ctx.req.getSession(false) != null){
            User currentUser = ctx.sessionAttribute("User");
            ArrayList<Account> accounts = new ArrayList<>();
            if (currentUser.userType.equals("Admin")) {
                accounts = accountService.getAllAccountsService();
            }
            else if (currentUser.userType.equals("Employee")) {
                accounts = accountService.getEmployeeAccountsService(currentUser.username);
            }
            else {
                accounts = accountService.getCustomerAccountsService(currentUser.username);
            }
            ctx.json(accounts);
            ctx.status(200);
        }
        else {
            ctx.status(401);
        }
    };

    private Handler getAccountRequests = (ctx) -> {
        //only admins/employees can view activeAccountRequests
        if (ctx.req.getSession(false) != null){
            User currentUser = ctx.sessionAttribute("User");
            boolean worked = true;
            if (currentUser.userType.equals("Admin")) {
                ArrayList<NewAccountRequest> nar = accountService.getAllNewAccountRequestsService();
                ctx.json(nar);
                ctx.status(200);
            }
            else if (currentUser.userType.equals("Employee")) {
                ArrayList<NewAccountRequest> nar = accountService.getEmployeeNewAccountRequestsService(currentUser.username);
                ctx.json(nar);
                ctx.status(200);
            }
            else {
                ctx.status(401);
            }
        }
        else {
            ctx.status(401);
        }
    };

    private Handler createAccount = (ctx) -> {
        //If a customer evokes this function, they will be able to create a new account request.
        //If an employee evokes this function they will be able to view all account requests from their employees
        //If an admin evokes this function they will be able to see all open account requests

        //first check to see if anyone is logged in
        if (ctx.req.getSession(false) != null){
            User currentUser = ctx.sessionAttribute("User");
            boolean worked = false;
            if (currentUser.userType.equals("Admin")) {
                ExerciseAccountRequest ear = ctx.bodyAsClass(ExerciseAccountRequest.class);
                worked = accountService.exerciseAccountRequestService(ear);
                if (worked) ctx.status(201); //request was exercised, either positively or negatively
                else ctx.status(400);
            }
            else if (currentUser.userType.equals("Employee")) {
                //check to see if the requestnumber in the given exerciseRequest is included in the employees request inbox.
                //if not, return an unauthorized status
                ExerciseAccountRequest ear = ctx.bodyAsClass(ExerciseAccountRequest.class);
                ArrayList<NewAccountRequest> nar = accountService.getEmployeeNewAccountRequestsService(currentUser.username);
                for (int i = 0; i < nar.size(); i++) {
                    if (nar.get(i).requestNumber == ear.requestNumber) worked = accountService.exerciseAccountRequestService(ear);
                }
                if (worked) ctx.status(201); //request was exercised, either positively or negatively
                else ctx.status(400);
            }
            else {
                NewAccountRequest nar = ctx.bodyAsClass(NewAccountRequest.class);
                worked = accountService.createNewAccountRequestService(nar);
                if (worked == true) ctx.status(201); //request created
                else ctx.status(400);
            }
        }
        else {
            ctx.status(401);
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        //Account Viewing
        app.get("/accounts", getAccounts);
        app.get("/accounts/requests", getAccountRequests);

        //Account Adding
        app.post("/accounts/requests", createAccount);

        //Account Editing
        //app.put("/accounts/{account_number}", alterAccount);
    }
}
