package com.revature.models.accounts;

import com.revature.models.users.Customer;

import java.util.Objects;

public abstract class Account {

    //I was thinking of putting a "User" field here, however, it doesn't make sense to me
    //for an account to have a user field, and for a user to also have an account field
    //as it would be redundant. Since user's can have multiple accounts it makes more
    //sense to me to link accounts to users on the user side. The case of joint accounts
    //which can have multiple users will be handled separately.

    //all types of the same account should have the same interest rate, but accounts of different types will have
    //different interest rates.
    public int accountNumber;
    //public static double interestRate;
    public String accountType;
    public double accountValue;
    public String accountOwner;

    public Account() {
        super();
    }
    public double getAccountValue () {
        return accountValue;
    }

    public void addFunds(double funds) {
        accountValue += funds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountNumber == account.accountNumber && Double.compare(account.accountValue, accountValue) == 0 && Objects.equals(accountType, account.accountType) && Objects.equals(accountOwner, account.accountOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accountType, accountValue, accountOwner);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", accountType='" + accountType + '\'' +
                ", accountValue=" + accountValue +
                ", accountOwner=" + accountOwner +
                '}';
    }
}
