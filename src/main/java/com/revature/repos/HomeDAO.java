package com.revature.repos;

import com.revature.models.Home;

import java.util.List;

public interface HomeDAO {
    //TODO: This was built from class example, will need to update for project
    public List<Home> findAll();
    public Home findByName(String name);
    public boolean updateHome(Home home);
    public boolean addHome(Home home);
}
