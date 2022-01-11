package com.revature.models.users;

import com.revature.models.accounts.Account;

import java.util.ArrayList;

public class Customer extends User {

    //TODO - currently each customer has a reference variable pointing to their employee, but each employee also has
    //TODO - reference variables pointing to their customers. Is it good form to have two objects pointing at eachother?
    private Employee assignedEmployee; //every user will have an employee at the bank responsible for their accounts, this will be assigned or reassigned by an admin
    private ArrayList<Account> activeAccounts; //a list of active accounts for the user

    //Customer specific information
    public double totalMoney; //The total amount of money across all accounts
    public double netWorth; //Customer's worth, including things not at bank. Used when creating new accounts

    //Constructors
    public Customer() {
        //the default constructor
        super();
    }

    public Customer(String userType, String firstName, String lastName, String username, String password) {
        super(userType, firstName, lastName, username);

        //need to encrypt the password before storing it
        this.password = encryptPassword(password);
    }

    @Override
    protected String encryptPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder(password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < encryptedPassword.length(); i++) {
            //add a value of 10 to each character in the password string to encrypt it. This high tech method of
            //encryption is said to be "un-hackable"
            newChar = encryptedPassword.charAt(i);
            newChar += 10;
            encryptedPassword.setCharAt(i, newChar);
        }
        log.info("Encrypted password for Customer: " + this.firstName + " " + this.lastName + " is " + encryptedPassword.toString());
        return encryptedPassword.toString();
    }

    @Override
    protected String getPassword() {
        StringBuilder decryptedPassword = new StringBuilder(this.password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < decryptedPassword.length(); i++) {
            //remove 10 from each character in the password to decrypt it
            newChar = decryptedPassword.charAt(i);
            newChar -= 10;
            decryptedPassword.setCharAt(i, newChar);
        }
        log.info("Actual password for Customer: " + this.firstName + " " + this.lastName + " is " + decryptedPassword.toString());
        return decryptedPassword.toString();
    }

    //Public Functions
    public void applyForAccount(String accountType) {
        //adds a request for a new account to the back of the queue of the employee that's responsible
        //for the customer
        NewAccountRequest req = new NewAccountRequest(this, accountType);
        assignedEmployee.addAccountRequest(req);
    }

    public void displayInfo() {
        System.out.println("User's name is " + this.firstName + ", " + this.lastName + ".");
        System.out.println("They currently have " + activeAccounts.size() + "accounts at the bank.");

        this.totalMoney = 0; //this variable just keeps track of value of accounts, the money isn't actually being deleted
        for (Account acc:activeAccounts) {
            System.out.println("    A " + acc.accountType);
            this.totalMoney += acc.getAccountValue();
        }

        System.out.println("The total value of the accounts is " + this.totalMoney);
    }

    public void addAccount(Account acc) {
        activeAccounts.add(acc);
    }

    public void addMoneyToAccount(int accountIndex, double amount) {

        //first, make sure that the desired account actually exists
        if (accountIndex < 0 || accountIndex >= activeAccounts.size()) {
            System.out.println("Account doesn't exist, please select a valid account.");
            return;
        }

        //second, check that the amount to be added is allowable (i.e. can't be <= 0)
        if (amount > 0) activeAccounts.get(accountIndex).addFunds(amount);
        else System.out.println("Invalid amount, funds not added to account.");
    }

}
