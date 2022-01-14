package com.revature.controllers;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Controller {
    public abstract void addRoutes(Javalin app);
    protected static Logger log = LoggerFactory.getLogger(Controller.class); //all Controller classes will have the same logger

    //this represents a global variable in Postman. Creating an instance of it here because it will be used in every controller
    //class and I don't want to make a typo somewhere. Spelling the variable wrong in one of the controller classes would cause
    //the variable to be set to null in Postman which might mess some things up.

    protected String postmanUsername = "postmanUsername";
}
