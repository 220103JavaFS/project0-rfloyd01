package com.revature.services;

import com.revature.dao.UserDAO;
import com.revature.models.users.User;

import java.util.ArrayList;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public ArrayList<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}
