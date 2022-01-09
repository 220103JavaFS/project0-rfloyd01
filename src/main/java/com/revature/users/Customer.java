package com.revature.users;

import com.revature.accounts.Account;
import com.revature.util.Message;
import java.util.ArrayList;

public class Customer extends User {

    private Employee assignedEmployee; //every user will have an employee at the bank responsible for their accounts
    public ArrayList<Account> activeAccounts; //a list of active accounts for the user

    void applyForAccount(String accountType) {
        //adds a request for a new account to the back of the queue of the employee that's responsible
        //for the customer
        NewAccountRequest req = new NewAccountRequest(this, accountType);
        assignedEmployee.addAccountRequest(req);
    }

    void displayInfo(){}
}
