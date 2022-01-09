package com.revature.accounts;

public abstract class Account {

    //I was thinking of putting a "User" field here, however, it doesn't make sense to me
    //for an account to have a user field, and for a user to also have an account field
    //as it would be redundant. Since user's can have multiple accounts it makes more
    //sense to me to link accounts to users on the user side. The case of joint accounts
    //which can have multiple users will be handled separately.

    double interestRate;

    public Account() {
        super();
    }
}
