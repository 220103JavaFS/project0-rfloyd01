package com.revature.users;

public class NewAccountRequest {
    Customer customer;
    String accountType;

    public NewAccountRequest(Customer c, String acc) {
        customer = c;
        accountType = acc;
    }
}