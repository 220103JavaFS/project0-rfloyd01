package com.revature.models.users;

import com.revature.models.util.Message;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Employee extends User {

    private ArrayList<Customer> assignedCustomers; //each employee will be responsible for a set amount of customers, this will be hanled by admins
    private ArrayDeque<NewAccountRequest> newAccountRequests; //this will be utilized like a queue to handle requests on a first come first served basis

    public void addCustomer(Customer cust) {
        assignedCustomers.add(cust);
    }

    public Employee() {

        super();
        assignedCustomers = new ArrayList<>(); //initialize list of customers
        newAccountRequests = new ArrayDeque<>(); //initialize account requests deque
    }

    public Employee(String userType, String firstName, String lastName, String username, String password) {
        super(userType, firstName, lastName, username, password);
        assignedCustomers = new ArrayList<>(); //initialize list of customers
        newAccountRequests = new ArrayDeque<>(); //initialize account requests deque
    }

    @Override
    public String encryptPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder(password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < encryptedPassword.length(); i++) {
            //add a value of 25 to each character in the password string to encrypt it. This high tech method of
            //encryption is said to be "un-hackable"
            newChar = encryptedPassword.charAt(i);
            newChar += 25;
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
            //remove 25 from each character in the password to decrypt it
            newChar = decryptedPassword.charAt(i);
            newChar -= 25;
            decryptedPassword.setCharAt(i, newChar);
        }
        //log.info("Actual password for Employee: " + this.firstName + " " + this.lastName + " is " + decryptedPassword.toString());
        return decryptedPassword.toString();
    }

    public void addAccountRequest(NewAccountRequest req) {
        //this function adds a new account request to the back of the employee's queue.
        //this function is called by a customer
        newAccountRequests.add(req);
    }

//    private void processAccountRequests() {
//        if (newAccountRequests.size() == 0) {
//            System.out.println("There are currently no new account requests to process.");
//            return;
//        }
//
//        NewAccountRequest currentRequest = new NewAccountRequest();
//        //The account request log is a queue so each request must be processed in the order that it
//        //comes in, no jumping around to easier requests!
//        while (newAccountRequests.size() > 0) {
//            currentRequest = newAccountRequests.remove(); //remove request from the front of the stack
//            System.out.println("Customer name: " + currentRequest.customer.lastName + ", " + currentRequest.customer.firstName);
//            System.out.println("Account type: " + currentRequest.accountType);
//
//            Scanner scan = new Scanner(System.in); //create a scanner to look at employee input
//            String answer = "";
//            boolean cont = true; //used to break out of below loop when ready
//
//            while (true) {
//                System.out.println("Type 'Yes' into the console to approve the request, 'No' to deny the request, or" +
//                        "type 'into' to see more information on the customer");
//                answer = scan.nextLine();
//                switch (answer) {
//                    case ("Yes"):
//                        createAccount(currentRequest.customer, currentRequest.accountType);
//
//                        //Send a message to the customer letting them know if the new account creation
//                        //TODO: I need to upcast the employee into a User in order to send the message but Java isn't liking that
//                        //TODO: presumably because User is abstract and can't exist. Do I need to rethink the Message constructor
//                        //TODO: or is there a way to do this?
//
////                        Message message = new Message("New account opened", "A new " + currentRequest.accountType + " account" +
////                                "has been opened for you.", (User)this);
//                }
//            }
//        }
//    }
//
//    private void createAccount (Customer c, String accountType) {
//        //invoke the Account Factory to create a new account and connect it to the appropriate customer
//        AccountFactory factory = AccountFactory.getFactory();
//        factory.addAccount(c, accountType);
//    }

    public void sendMessage (User u, Message m) {

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
