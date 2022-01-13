package com.revature.models.users;

import java.util.ArrayList;

import com.revature.models.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class User {

    //information fields for the user
    public String userType;
    public String firstName;
    public String lastName;
    public String username;
    public String password; //it's ok to have this as public because the passwords are encrypted
    protected static Logger log = LoggerFactory.getLogger(User.class); //create a separate logger for all users
    private ArrayList<Message> inbox; //every user has an inbox which can hold messages that only they can access

    //Constructors
    public User() {
        //the default constructor
        super();
    }
    public User (String userType, String firstName, String lastName, String username) {
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        inbox = new ArrayList<>(); //create the user's message inbox
    }

    void readMessage(Message m) {
        System.out.println(m.title);
        System.out.println("------------------");
        System.out.println(m.messageBody);
    }

    public void receiveMessage(Message m) {
        this.inbox.add(m);
    }


    void setPassword(String p) {
        password = p;
    }

    protected abstract String encryptPassword(String password); //encrypts password on creation before storing in database for security reasons, different types of users will have different types of encryption
    protected abstract String getPassword(); //decrypts the password and returns the string
    public boolean comparePassword(String passwordAttempt) {
        //decrypts an encrypted password to see if it matches the password given to function
        if (getPassword().equals(passwordAttempt)) return true; //the passwordAttempt string matches the decrypted password
        return false; //no match, invalid login attempt
    }

}
