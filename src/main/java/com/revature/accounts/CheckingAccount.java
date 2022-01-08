package com.revature.accounts;

import com.revature.users.User;

public class CheckingAccount extends Account {

    User accountOwner;

    public CheckingAccount()
    {
        super();
        interestRate = 0.01;
    }
}
