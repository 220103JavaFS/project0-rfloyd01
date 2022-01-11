package com.revature.models.users;

import com.revature.models.accounts.AccountFactory;
import com.revature.util.Message;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public class Employee extends User {

    private ArrayList<Customer> assignedCustomers; //each employee will be responsible for a set amount of customers
    private Queue<NewAccountRequest> newAccountRequests; //a list of account requests needing approval, first come first served

    void addCustomer(Customer cust) {
        assignedCustomers.add(cust);
    }

    public Employee() {
        super();
    }

    public Employee(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public void addAccountRequest(NewAccountRequest req) {
        //this function adds a new account request to the back of the employee's queue.
        //this function is called by a customer
        newAccountRequests.add(req);
    }

    private void processAccountRequests() {
        if (newAccountRequests.size() == 0) {
            System.out.println("There are currently no new account requests to process.");
            return;
        }

        NewAccountRequest currentRequest = new NewAccountRequest();
        //The account request log is a queue so each request must be processed in the order that it
        //comes in, no jumping around to easier requests!
        while (newAccountRequests.size() > 0) {
            currentRequest = newAccountRequests.remove(); //remove request from the front of the stack
            System.out.println("Customer name: " + currentRequest.customer.lastName + ", " + currentRequest.customer.firstName);
            System.out.println("Account type: " + currentRequest.accountType);

            Scanner scan = new Scanner(System.in); //create a scanner to look at employee input
            String answer = "";
            boolean cont = true; //used to break out of below loop when ready

            while (true) {
                System.out.println("Type 'Yes' into the console to approve the request, 'No' to deny the request, or" +
                        "type 'into' to see more information on the customer");
                answer = scan.nextLine();
                switch (answer) {
                    case ("Yes"):
                        createAccount(currentRequest.customer, currentRequest.accountType);

                        //Send a message to the customer letting them know if the new account creation
                        //TODO: I need to upcast the employee into a User in order to send the message but Java isn't liking that
                        //TODO: presumably because User is abstract and can't exist. Do I need to rethink the Message constructor
                        //TODO: or is there a way to do this?

//                        Message message = new Message("New account opened", "A new " + currentRequest.accountType + " account" +
//                                "has been opened for you.", (User)this);
                }
            }
        }
    }

    private void createAccount (Customer c, String accountType) {
        //invoke the Account Factory to create a new account and connect it to the appropriate customer
        AccountFactory factory = AccountFactory.getFactory();
        factory.addAccount(c, accountType);
    }

    public void sendMessage (User u, Message m) {

    }
}
