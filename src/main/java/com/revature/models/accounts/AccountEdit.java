package com.revature.models.accounts;

import java.util.Objects;

public class AccountEdit {
    //this class allows for depositing and withdrawing from accounts
    public String action;
    public double amount;

    public AccountEdit() {super();}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEdit that = (AccountEdit) o;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, amount);
    }

    @Override
    public String toString() {
        return "AccountEdit{" +
                "action='" + action + '\'' +
                ", amount=" + amount +
                '}';
    }
}
