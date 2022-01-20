package com.revature.models.users;

import com.revature.controllers.Controller;
import com.revature.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class UserFactory {

    //This will be a singleton class

    private Logger log = LoggerFactory.getLogger(UserFactory.class);
    private static UserFactory factory;
    private UserDAO userDAO = new UserDAO();

    private UserFactory() {
        super();
    }

    public User makeUser(UserRequest u) {
        if (u.userType.equals("Customer")) {
            //before the customer is finished we need to assign them to an employee
            //if no employees currently exist in the database return null which will trigger an error elsewhere
            Customer cust = new Customer(u.userType, u.firstName, u.lastName, u.username, u.password);
            ArrayList<Employee> allCurrentEmployees = userDAO.getAllEmployeesDAO();
            if (allCurrentEmployees.size() == 0) {
                log.info("Currently no employees exist so one couldn't be assigned to the new Customer");
                cust.setAssignedEmployee(null);
            }
            else {
                //pick one of the employees at random to assign
                int randomEmployee = (int)(Math.random() * 100) % allCurrentEmployees.size();
                cust.setAssignedEmployee(allCurrentEmployees.get(randomEmployee).username);
            }
            return cust;
        }
        else if (u.userType.equals("Employee")) {
            return new Employee(u.userType,u.firstName, u.lastName, u.username, u.password);
        }
        else return new Admin(u.userType,u.firstName, u.lastName, u.username, u.password);
    }

    public static UserFactory getFactory() {
        if (factory != null) {
            return factory;
        }
        factory = new UserFactory();
        return factory;
    }
}