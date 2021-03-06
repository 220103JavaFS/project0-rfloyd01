package com.revature.controllers;

import com.revature.models.accounts.Account;
import com.revature.models.accounts.AccountEdit;
import com.revature.models.accounts.ExerciseAccountRequest;
import com.revature.models.accounts.NewAccountRequest;
import com.revature.models.users.Customer;
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

    //GET HANDLERS
    private Handler getAccounts = (ctx) -> {
        //If a customer envokes this function, they will be able to see all of their accounts.
        //If an employee evokes this function, they will be able to see all accounts owned by their customers
        //Admins can see all existing accounts when calling this function

        //first check to see if anyone is logged in
        if (ctx.req.getSession(false) != null){
            User currentUser = ctx.sessionAttribute("User");
            ArrayList<Account> accounts;
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
    private Handler getSpecificAccount = (ctx) -> {
        //Get's info on the specific account, but only if the user has access to it

        //first check to see if anyone is logged in
        if (ctx.req.getSession(false) != null){
            User currentUser = ctx.sessionAttribute("User");
            ArrayList<Account> accounts = accountService.getAllAccountsService();
            Integer accountNumber = Integer.parseInt(ctx.pathParam("account_number")); //get account_number as an integer
            boolean found = false;
            if (currentUser.userType.equals("Admin")) {
                //admins have access to all accounts, cycle through all the accounts to see if the desired account is there
                for (int i = 0; i < accounts.size(); i++) {
                    if (accounts.get(i).accountNumber == accountNumber) {
                        ctx.json(accounts.get(i));
                        found = true;
                        break;
                    }
                }
                if (found) ctx.status(200);
                else ctx.status(404);
            }
            else if (currentUser.userType.equals("Employee")) {
                //employees can only see accounts of their customers. get customer by call to user service
                Employee currentEmployee = userService.getEmployeeService(currentUser.username);
                boolean allowed = false;
                for (int i = 0; i < accounts.size(); i++) {
                    if (accounts.get(i).accountNumber == accountNumber) {
                        //account is found, make sure that employee has access to it
                        found = true;
                        ArrayList<Customer> customers = currentEmployee.getAssignedCustomers();
                        for (int j = 0; j < customers.size(); j++) {
                            if (customers.get(j).username.equals(accounts.get(i).accountOwner)) {
                                ctx.json(accounts.get(i));
                                ctx.status(200);
                                allowed = true;
                                break;
                            }
                        }
                    }
                }
                if (!found) ctx.status(404);
                else if (!allowed) ctx.status(401);
            }
            else {
                //users can only see accounts that they own
                log.info("Customer account get called:");
                boolean allowed = false;
                for (int i = 0; i < accounts.size(); i++) {
                    if (accounts.get(i).accountNumber == accountNumber) {
                        found = true;
                        if (accounts.get(i).accountOwner.equals(currentUser.username)) {
                            ctx.json(accounts.get(i));
                            ctx.status(200);
                            allowed = true;
                            break;
                        }
                    }
                }
                if (!found) ctx.status(404);
                else if (!allowed) ctx.status(401);
            }
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

    //CREATE HANDLERS
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
                log.info("admin " + currentUser.username + "is attempting to exercise account request " + ear.requestNumber);
                worked = accountService.exerciseAccountRequestService(ear);
                if (worked) ctx.status(201); //request was exercised, either positively or negatively
                else ctx.status(400);
            }
            else if (currentUser.userType.equals("Employee")) {
                //check to see if the requestnumber in the given exerciseRequest is included in the employees request inbox.
                //if not, return an unauthorized status
                ExerciseAccountRequest ear = ctx.bodyAsClass(ExerciseAccountRequest.class);
                log.info("employee " + currentUser.username + "is attempting to exercise account request " + ear.requestNumber);
                ArrayList<NewAccountRequest> nar = accountService.getEmployeeNewAccountRequestsService(currentUser.username);
                for (int i = 0; i < nar.size(); i++) {
                    if (nar.get(i).requestNumber == ear.requestNumber) worked = accountService.exerciseAccountRequestService(ear);
                }
                if (worked) ctx.status(201); //request was exercised, either positively or negatively
                else ctx.status(400);
            }
            else {
                NewAccountRequest nar = ctx.bodyAsClass(NewAccountRequest.class);
                log.info("user " + currentUser.username + "is attempting to open a new " + nar.accountType + " account.");
                worked = accountService.createNewAccountRequestService(nar);
                if (worked == true) ctx.status(201); //request created
                else ctx.status(400);
            }
        }
        else {
            ctx.status(401);
        }
    };

    //EDIT HANDLERS
    private Handler editAccount = (ctx) -> {
        //first check to see if anyone is logged in
        if (ctx.req.getSession(false) != null){
            //then, we need to check that the account number entered in the account edit request actually exists
            AccountEdit ae = ctx.bodyAsClass(AccountEdit.class);
            Integer accountNumber = Integer.parseInt(ctx.pathParam("account_number")); //get account_number as an integer

            Account account = accountService.getAccountService(accountNumber);

            //log.info("editAccount called");
            if (account != null) {
                User currentUser = ctx.sessionAttribute("User");
                boolean worked = false; //default to false
                if (currentUser.userType.equals("Admin")) {
                    //admins can edit any persons account
                    log.info("admin " + currentUser.username + "is attempting to edit account " + account.accountNumber);
                    worked = accountService.editAccountService(ae, account);
                    if (worked) ctx.status(201); //edit was carried out
                    else ctx.status(400); //something was wrong with the edit request
                }
                else if (currentUser.userType.equals("Employee")) {
                    //employees can only edit their customer's accounts
                    ArrayList<Account> accounts = accountService.getEmployeeAccountsService(currentUser.username);
                    log.info("employee " + currentUser.username + "is attempting to edit account " + account.accountNumber);
                    for (int i = 0; i < accounts.size(); i++) {
                        if (accounts.get(i).accountNumber == account.accountNumber) worked = accountService.editAccountService(ae, account);
                    }
                    if (worked) ctx.status(201); //edit was carried out
                    else ctx.status(400); //something was wrong with the edit request
                }
                else {
                    //customers can only edit their own accounts
                    log.info("customer " + currentUser.username + "is attempting to edit account " + account.accountNumber);
                    if (account.accountOwner.equals(currentUser.username)) worked = accountService.editAccountService(ae, account);
                    if (worked == true) ctx.status(201); //edit was carried out
                    else ctx.status(400); //something was wrong with the edit request
                }
            }
            else {
                ctx.status(404); //couldn't find account
            }
        }
        else {
            ctx.status(401); //nobody was logged in
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        //Account Viewing
        app.get("/accounts", getAccounts);
        app.get("/accounts/{account_number}", getSpecificAccount);
        app.get("/accounts/requests", getAccountRequests);

        //Account Adding
        app.post("/accounts/requests", createAccount);

        //Account Editing
        app.put("/accounts/{account_number}", editAccount);
    }
}
