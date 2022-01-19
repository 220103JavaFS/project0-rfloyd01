package com.revature.models.users;

import com.revature.models.accounts.Account;

import java.util.ArrayList;

public class Customer extends User {

    //TODO - currently each customer has a reference variable pointing to their employee, but each employee also has
    //TODO - reference variables pointing to their customers. Is it good form to have two objects pointing at eachother?
    private Employee assignedEmployee; //every user will have an employee at the bank responsible for their accounts, this will be assigned or reassigned by an admin
    private ArrayList<Account> activeAccounts; //a list of active accounts for the user

    //Customer specific information
    public int activeAccountRequests =  0; //a user won't start with an open account requests
    public double totalMoney = 0; //The total amount of money across all accounts
    public double netWorth; //Customer's worth, including things not at bank. Used when creating new accounts

    //Constructors
    public Customer() {
        //the default constructor
        super();
    }

    public Customer(String userType, String firstName, String lastName, String username, String password) {
        super(userType, firstName, lastName, username, password);

        //also need to create a list to hold accounts (which will be an empty list upon creation)
        activeAccounts = new ArrayList<>();
    }

    public Employee getAssignedEmployee() {return assignedEmployee;}

    public void setAssignedEmployee(Employee emp) {
        assignedEmployee = emp;
    }

    @Override
    public String encryptPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder(password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < encryptedPassword.length(); i++) {
            //add a value of 10 to each character in the password string to encrypt it. This high tech method of
            //encryption is said to be "un-hackable"
            newChar = encryptedPassword.charAt(i);
            newChar += 10;
            encryptedPassword.setCharAt(i, newChar);
        }
        //log.info("Encrypted password for Customer: " + this.firstName + " " + this.lastName + " is " + encryptedPassword.toString());
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
        //log.info("Actual password for Customer: " + this.firstName + " " + this.lastName + " is " + decryptedPassword.toString());
        return decryptedPassword.toString();
    }

    //Public Functions
    public void applyForAccount(String accountType) {
        //adds a request for a new account to the back of the queue of the employee that's responsible
        //for the customer
        NewAccountRequest req = new NewAccountRequest(this, accountType);
        assignedEmployee.addAccountRequest(req);
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

    @Override
    public String toString() {
        //print all the basic fields that a user has (minus the password) as well as a list of all
        //active accounts for the customer
        return "User{" +
                "userType='" + userType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", activeAccounts=" + activeAccounts +
                '}';
    }
}
