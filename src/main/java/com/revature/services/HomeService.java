package com.revature.services;

import com.revature.models.Home;
import com.revature.repos.HomeDAO;
import com.revature.repos.HomeDAOImpl;

import java.util.List;

public class HomeService {

    private HomeDAO homeDAO = new HomeDAOImpl();

    public List<Home> findAllHomes() {
        return homeDAO.findAll();
    }

    public Home findHome(String name) {
        return homeDAO.findByName(name);
    }

    public boolean updateHome(Home home) {
        return homeDAO.updateHome(home);
    }

    public boolean addHome(Home home) {
        return homeDAO.addHome(home);
    }
}
