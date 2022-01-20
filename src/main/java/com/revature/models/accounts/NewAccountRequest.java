package com.revature.models.accounts;

import java.util.Objects;

public class NewAccountRequest {
    public int requestNumber;
    public String customerName;
    public String accountType;

    public NewAccountRequest() {
        super();
    }
    public NewAccountRequest(String c, String acc) {
        customerName = c;
        accountType = acc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewAccountRequest that = (NewAccountRequest) o;
        return requestNumber == that.requestNumber && Objects.equals(customerName, that.customerName) && Objects.equals(accountType, that.accountType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestNumber, customerName, accountType);
    }

    @Override
    public String toString() {
        return "NewAccountRequest{" +
                "requestNumber=" + requestNumber +
                ", customerName='" + customerName + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}
