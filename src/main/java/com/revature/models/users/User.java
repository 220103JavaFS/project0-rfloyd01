package com.revature.models.users;

import java.util.ArrayList;
import java.util.Scanner;

import com.revature.util.Message;

public abstract class User {

    public String username;
    private String password; //this will need some form of encryption

    //public information about the user
    public String firstName;
    public String lastName;

    private ArrayList<Message> inbox; //every user has an inbox which can hold messages that only they can access

    //Constructors
    public User() {
        //the default constructor
        super();
    }
    public User (String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    void readMessage(Message m) {
        System.out.println(m.title);
        System.out.println("------------------");
        System.out.println(m.messageBody);
    }

    public void receiveMessage(Message m) {
        this.inbox.add(m);
    }

//    public User createNewUser() {
//        //regardless of what kind of account is being made (customer, employee, admin, etc.), initial account
//        //creation will happen the same way
//
//        //in order to create an account, a name must be given, as well as a username and password
//        //the username will be checked against others in the system to make sure it isn't already
//        //taken
//        this.firstName = firstName;
//        this.lastName = lastName;
//
//        //create a scanner to read username and password attempts
//        Scanner scan = new Scanner(System.in);
//        boolean valid = false;
//        String attempt = ""; //initialize to blank string, it must be filled before being utilized
//
//        while (!valid) {
//            //The new user will be prompted to create a username, if the username is already in the database they
//            //will be prompted to try again until a suitable name is found
//
//            System.out.println("Please enter a username for the account");
//            attempt = scan.nextLine();
//            //TODO - Create a function that checks database to see if username is available
//            //valid = usernameAvailable(attempt); //if username isn't available this function will also print "Username taken"
//
//            //for now just accept any username to avoid an infinite loop here
//            valid = true;
//        }
//
//        this.username = attempt;
//
//        System.out.println("Username created successfully. Please enter a password for the account.");
//        System.out.println("The password must be at least 10 characters long, contain a number, a lower " +
//                "case letter, an upper case letter and a special character (e.g. !, @, &)");
//
//        valid = false;
//        while (!valid) {
//            attempt = scan.nextLine();
//
//            //don't nest the "if" statements here as each block must be evaluated
//            if (attempt.length() < 10) {
//                System.out.println("The password must be at least 10 characters long, please try again. ");
//                continue;
//            }
//
//            //look through the password one letter at a time and see if each criteria has been met. Track
//            //the criteria using a 4 bit number. If each bit is set from a 0 to a 1 then all the criteria
//            //have been met
//            int criteriaMet = 0;
//            for (int i = 0; i < attempt.length(); i++) {
//                char c = attempt.charAt(i);
//
//                if (c >= 'A' && c <= 'Z') criteriaMet |= 0b1; //uppercase criteria met
//                else if (c >= 'a' && c <= 'z') criteriaMet |= 0b10; //lowercase criteria met
//                else if (c >= '0' && c <= '9') criteriaMet |= 0b100; //number criteria met
//                else criteriaMet |= 0b1000; //special character criteria met TODO - may want to consider limiting which characters count here
//
//                if (criteriaMet == 0b1111) {
//                    //can break out as soon as all criteria are met without checking whole password
//                    //TODO - if i decide to limit what the special character can be then the whole password will need
//                    //TODO - to be checked and this if block will go away
//                    valid = true;
//                    break;
//                }
//            }
//
//            if (!valid) System.out.println("Sorry, that password didn't meet all the correct criteria, please try again.");
//        }
//
//        this.setPassword(attempt); //set the new password
//        System.out.println("Password created successfully, thanks for creating your profile.");
//    }

    void setPassword(String p) {
        password = p;
    }

}
