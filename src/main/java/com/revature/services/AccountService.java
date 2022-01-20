package com.revature.services;

import com.revature.dao.AccountDAO;
import com.revature.dao.UserDAO;
import com.revature.models.accounts.Account;
import com.revature.models.accounts.ExerciseAccountRequest;
import com.revature.models.accounts.NewAccountRequest;
import com.revature.models.users.Customer;
import com.revature.models.users.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class AccountService {
    //similar to the UserService, this class handles all logic with individual accounts
    //there are multiple controller classes that may need access to accounts so this class
    //is a singleton

    private AccountDAO accountDAO = new AccountDAO();
    private UserDAO userDAO = new UserDAO();
    private static Logger log = LoggerFactory.getLogger(AccountService.class); //Do all classes get their own logger?

    //GET FUNCTIONS
    public ArrayList<Account> getAllAccountsService() {
        return accountDAO.getAllAccountsDAO();
    }
    public ArrayList<Account> getEmployeeAccountsService(String employeeUsername) {
        return accountDAO.getEmployeeAccountsDAO(employeeUsername);
    }
    public ArrayList<Account> getCustomerAccountsService(String customerUsername) {
        return accountDAO.getCustomerAccountsDAO(customerUsername);
    }
    public ArrayList<NewAccountRequest> getEmployeeNewAccountRequestsService(String employeeUsername) {
        return accountDAO.getEmployeeAccountRequestsDAO(employeeUsername);
    }
    public ArrayList<NewAccountRequest> getAllNewAccountRequestsService() {
        //calls the getEmployeeNewAccountRequestsService() function for all employees currently in the database
        ArrayList<Employee> employees = userDAO.getAllEmployeesDAO();
        ArrayList<NewAccountRequest> nar = new ArrayList<>();
        for (int i = 0; i < employees.size(); i++) {
            nar.addAll(getEmployeeNewAccountRequestsService(employees.get(i).username));
        }

        return nar;
    }

    //CREATE FUNCTIONS
    public boolean createNewAccountRequestService(NewAccountRequest nar) {
        log.info("create accountRequestService called");
        return accountDAO.createNewAccountRequestDAO(nar);
    }

    public boolean exerciseAccountRequestService(ExerciseAccountRequest ear) {
        //look at the request, if the exercise string is Yes, then remove the request from the database and create a new account, then assign it to the user
        //if the exercise string is no, remove the request from the database, but don't create a new account
        //if the string is something else then just return an error
        if (ear.decision.equals("Yes")) {
            NewAccountRequest nar = accountDAO.getAccountRequest(ear.requestNumber); //retrieve the request from the db
            accountDAO.deleteAccountRequestDAO(ear.requestNumber); //delete the request from the database
            accountDAO.createNewAccount(nar);
            return true; //new account created
        }
        else if (ear.decision.equals("No")) {
            accountDAO.deleteAccountRequestDAO(ear.requestNumber); //delete the request from the database
            return true; //option was exercised, it was just exercised negatively
        }
        else {
            return false;
        }
    }
}
