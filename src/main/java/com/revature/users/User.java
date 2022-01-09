package com.revature.users;

import java.util.ArrayList;

import com.revature.util.Message;

public abstract class User {

    public String username;
    private String password; //this will need some form of encryption

    //public information about the user
    public String firstName;
    public String lastName;

    private ArrayList<Message> inbox; //every user has an inbox which can hold messages that only they can access

    void readMessage(Message m) {
        System.out.println(m.title);
        System.out.println("------------------");
        System.out.println(m.messageBody);
    }

}
