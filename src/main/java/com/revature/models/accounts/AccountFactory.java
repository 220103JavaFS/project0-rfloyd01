package com.revature.models.accounts;

import com.revature.models.users.Customer;

public class AccountFactory {

    //This will be a singleton class

    private static AccountFactory factory;

    private AccountFactory() {
        super();
    }

    public void addAccount(Customer c, String accountType) {
        //this is the function that gets called to initiate the factory
        //A customer is passed so that the account has a home (there can't be an account
        //that's unattached to a customer) and a string with the account type is also passed.
        //If the passed string doesn't match any of the possible account types then don't create
        //anything and display a warning message.


        if (accountType == "Checking") {
            CheckingAccount newChecking = (CheckingAccount) makeAccount(accountType);
            c.addAccount(newChecking);
        }
        else if (accountType == "Saving") {
            SavingAccount newSaving = (SavingAccount) makeAccount(accountType);
            c.addAccount(newSaving);
        }
        else System.out.println("An account of that type doesn't exist, couldn't create account.");
    }
    private Account makeAccount(String s) {
        //only the Account Factory can call this function. This constructor can only have correct strings passed
        //to it, so it's not necessary to perform a check here
        if (s == "Checking") return new CheckingAccount();
        else return new SavingAccount();
    }

    public static AccountFactory getFactory() {
        if (factory == null) {
            factory = new AccountFactory();
        }

        return factory;
    }
}
