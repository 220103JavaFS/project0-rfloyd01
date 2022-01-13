package com.revature.models.util;

import java.util.Objects;

public class JSONResponse {
    //for now, each JSON response that I can send back to Postman will be composed of two different entities,
    //a message and a command. Both of these are strings.
    public String newValue;
    public String messageBody;

    public JSONResponse() {
        newValue = "";
        messageBody = "";
    }
    public JSONResponse(String message) {
        this.messageBody = message;
    }
    public JSONResponse(String message, String command) {
        this(message);
        this.newValue = command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSONResponse that = (JSONResponse) o;
        return Objects.equals(newValue, that.newValue) && Objects.equals(messageBody, that.messageBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newValue, messageBody);
    }

    @Override
    public String toString() {
        return "JSONResponse{" +
                "command='" + newValue + '\'' +
                ", jsonMessage='" + messageBody + '\'' +
                '}';
    }


}
