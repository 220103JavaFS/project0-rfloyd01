package com.revature.controllers;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Controller {
    public abstract void addRoutes(Javalin app);
    protected static Logger log = LoggerFactory.getLogger(Controller.class); //all Controller classes will have the same logger
}
