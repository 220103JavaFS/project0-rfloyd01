package com.revature.models.users;

import com.revature.models.accounts.Account;

import java.util.ArrayList;

public class Customer extends User {
    private String assignedEmployee; //the username of the employee assigned to this customer, assigned upon customer creation
    private ArrayList<Account> activeAccounts; //a list of active accounts for the user

    public String getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(String assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    //Constructors
    public Customer() {
        //the default constructor
        super();
        activeAccounts = new ArrayList<>();
    }

    public Customer(String userType, String firstName, String lastName, String username, String password) {
        super(userType, firstName, lastName, username, password);

        //also need to create a list to hold accounts (which will be an empty list upon creation)
        activeAccounts = new ArrayList<>();
    }

    @Override
    public String encryptPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder(password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < encryptedPassword.length(); i++) {
            //removes a value of 5 from each character in the password string to encrypt it. This high tech method of
            //encryption is said to be "un-hackable"
            newChar = encryptedPassword.charAt(i);
            newChar -= 5;
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
            //add 5 to each character in the password to decrypt it
            newChar = decryptedPassword.charAt(i);
            newChar += 5;
            decryptedPassword.setCharAt(i, newChar);
        }
        //log.info("Actual password for Customer: " + this.firstName + " " + this.lastName + " is " + decryptedPassword.toString());
        return decryptedPassword.toString();
    }


    public ArrayList<Account> getActiveAccounts() {
        return activeAccounts;
    }

    public void setActiveAccounts(ArrayList<Account> activeAccounts) {
        this.activeAccounts = activeAccounts;
    }

    //Public Functions


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
