package com.revature.models.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    //this class will be used to connect with our database

    public static Connection getConnection() throws SQLException {
        //For many frameworks using JDBC it's necessary to register the driver
        //package you're using. This is to make the framework aware of it (Javalin in this case).
        try {
            Class.forName(("org.postgresql.Driver"));
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //To connect to the database, we're going to need the location, the database name and the username / password for the database

        //the below string is comprised of a few parts:
        //1. The beginning statement -> jdbc:postgresql://
        //2. The endpoint for the db -> javafs220103.csjukvpunyct.us-east-1.rds.amazonaws.com
        //3. The port number for the db -> :5432
        //4. The name of the specific db -> /demos
        String url = "jdbc:postgresql://javafs220103.csjukvpunyct.us-east-1.rds.amazonaws.com:5432/demos";

        //We can set the below variables by going to Run -> Edit Configurations -> App (or whatever your main driver is called) -> Environment Variables. We can then get these variables with: System.getenv("var-name")
        //TODO: Currently I can connect when using normal strings but not when trying to use environment variables, try to fix this at some point.
        String username = System.getenv("DB_USERNAME"); //The username given to the overall db when it was created. Note* it's possible and preferable to hide this environment in environment variables for security reasons
        String password = System.getenv("DB_PASSWORD"); //The password for the db. Note* it's possible and preferable to hide this environment in environment variables for security reasons

        System.out.println("Environment username = " + username);
        System.out.println("Environment password = " + password);
        //String username = "postgres"; //The username given to the overall db when it was created. Note* it's possible and preferable to hide this environment in environment variables for security reasons
        //String password = "password"; //The password for the db. Note* it's possible and preferable to hide this environment in environment variables for security reasons
        return DriverManager.getConnection(url, username, password); //this can throw a SQLException so we add throws SQLException to our class definition

    }

}
