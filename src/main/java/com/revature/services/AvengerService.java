package com.revature.services;

import com.revature.models.Avenger;
import com.revature.repos.AvengerDAO;
import com.revature.repos.AvengerDAOImpl;

public class AvengerService {
    private AvengerDAO avengerDao = new AvengerDAOImpl();

    public Avenger callAvenger(int id) {
        return avengerDao.findHero(id);
    }
}
