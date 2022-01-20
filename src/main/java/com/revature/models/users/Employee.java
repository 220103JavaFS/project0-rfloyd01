package com.revature.models.users;

import com.revature.models.accounts.NewAccountRequest;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Employee extends User {

    private ArrayList<Customer> assignedCustomers; //each employee will be responsible for a set amount of customers, this will be hanled by admins

    public ArrayList<Customer> getAssignedCustomers() {
        return assignedCustomers;
    }

    public void setAssignedCustomers(ArrayList<Customer> assignedCustomers) {
        this.assignedCustomers = assignedCustomers;
    }


    public void addCustomer(Customer cust) {
        assignedCustomers.add(cust);
    }

    public Employee() {

        super();
        assignedCustomers = new ArrayList<>(); //initialize list of customers
    }

    public Employee(String userType, String firstName, String lastName, String username, String password) {
        super(userType, firstName, lastName, username, password);
        assignedCustomers = new ArrayList<>(); //initialize list of customers
    }

    @Override
    public String encryptPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder(password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < encryptedPassword.length(); i++) {
            //remove a value of 8 from each character in the password string to encrypt it. This high tech method of
            //encryption is said to be "un-hackable"
            newChar = encryptedPassword.charAt(i);
            newChar -= 8;
            encryptedPassword.setCharAt(i, newChar);
        }
        //log.info("Encrypted password for Employee: " + this.firstName + " " + this.lastName + " is " + encryptedPassword.toString());
        return encryptedPassword.toString();
    }

    @Override
    protected String getPassword() {
        StringBuilder decryptedPassword = new StringBuilder(this.password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < decryptedPassword.length(); i++) {
            //add 8 from each character in the password to decrypt it
            newChar = decryptedPassword.charAt(i);
            newChar += 8;
            decryptedPassword.setCharAt(i, newChar);
        }
        //log.info("Actual password for Employee: " + this.firstName + " " + this.lastName + " is " + decryptedPassword.toString());
        return decryptedPassword.toString();
    }

    @Override
    public String toString() {
        //print out all of the basic fields of a user (minus the password) as well as a list of all
        //the employees customers
        return "User{" +
                "userType='" + userType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", assignedCustomers=" + assignedCustomers +
                '}';
    }
}
