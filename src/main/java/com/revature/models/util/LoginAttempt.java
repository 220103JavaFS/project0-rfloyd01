package com.revature.models.util;

public class LoginAttempt {
    //TODO: Being new to this I'm not sure if something like a login attempt is worthy of getting it's own class
    //  maybe build out the application a little more and then try to figure out of this is necessary or not

    //The only two things that you need to attempt to login to the application are a username and a password
    //this login attempt will be passed on down to the DAO layer which will check that the username exists
    //and that the password is correct

    //public String userType;
    public String username;
    public String password; //this will get encrypted so ok to leave public

    public LoginAttempt() {super();}
    public LoginAttempt(String ut, String u, String p) {
        //this.userType = ut;
        this.username = u;
        this.password = p;
    }
}
