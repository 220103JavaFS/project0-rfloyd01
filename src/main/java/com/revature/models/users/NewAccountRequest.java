package com.revature.models.users;

public class NewAccountRequest {
    Customer customer;
    String accountType;

    public NewAccountRequest() {
        super();
    }
    public NewAccountRequest(Customer c, String acc) {
        customer = c;
        accountType = acc;
    }
}
