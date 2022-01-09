package com.revature.users;

import java.util.ArrayList;
import java.util.Queue;

public class Employee {

    private ArrayList<Customer> assignedCustomers; //each employee will be responsible for a set amount of customers
    private Queue<NewAccountRequest> newAccountRequests; //a list of account requests needing approval, first come first served

    void addCustomer(Customer cust) {
        assignedCustomers.add(cust);
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

        //The account request log is a queue so each request must be processed in the order that it
        //comes in, no jumping around to easier requests!
        while (newAccountRequests.size() > 0) {

        }
    }
}
