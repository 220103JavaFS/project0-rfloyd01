package com.revature.util;

import com.revature.users.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    public String title;
    public String from;
    public String timeStamp;
    public String messageBody;

    public Message (String t, String mb, User f) {
        title = t;
        messageBody = mb;
        from = f.lastName+", "+f.firstName;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        timeStamp = dtf.format(now);

    }

}
