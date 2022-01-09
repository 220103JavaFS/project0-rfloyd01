package com.revature.users;

import com.revature.accounts.Account;
import com.revature.util.Message;
import java.util.ArrayList;
import java.util.Scanner;

public class Customer extends User {

    //TODO - currently each customer has a reference variable pointing to their employee, but each employee also has
    //TODO - reference variables pointing to their customers. Is it good form to have two objects pointing at eachother?
    private Employee assignedEmployee; //every user will have an employee at the bank responsible for their accounts
    private ArrayList<Account> activeAccounts; //a list of active accounts for the user

    //Customer specific information
    public double totalMoney; //The total amount of money across all accounts
    public double netWorth; //Customer's worth, including things not at bank. Used when creating new accounts

    //Constructors
    public Customer() {
        //the default constructor
        super();
    }

    public Customer(String firstName, String lastName, ArrayList<Employee> employees) {
        super(firstName, lastName); //first, call the default User constructor which handles name, username and password

        //There need to be some existing employees at the bank before customers can start making accounts.
        //The list of employees is passed to the Customer constructor so that a customer can be assigned
        //to a random employee who will handle their accounts. A User factory will make sure that employees
        // actually exist before allowing customers to be created
        int employeeNumber = (int)(Math.random() * employees.size());
        this.assignedEmployee = employees.get(employeeNumber);
    }

    public Customer(String firstName, String lastName, ArrayList<Employee> employees, String accountType, double startingAmount) {
        //a user can choose to open an account with the bank when creating their profile, although, they don't have to
        this(firstName, lastName, employees); //constructor chaining with non-account creation constructor

        //a customer can't directly open an account, they apply to have one opened and the employee opens it for them
        applyForAccount(accountType);
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
