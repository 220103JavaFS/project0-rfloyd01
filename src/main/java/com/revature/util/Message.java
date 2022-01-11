package com.revature.util;

import com.revature.models.users.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    public String title;
    public String from;
    public String timeStamp;
    public String messageBody;

    public Message (String t, String mb, User f) {
        //User is an abstract class, when calling this function a non-abstract user must be created and upcast into this role
        title = t;
        messageBody = mb;
        from = f.lastName+", "+f.firstName;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        timeStamp = dtf.format(now);

    }

}
