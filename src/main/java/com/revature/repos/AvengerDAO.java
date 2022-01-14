package com.revature.repos;

import com.revature.models.Avenger;

public interface AvengerDAO {

    public Avenger findHero(int id);
    public boolean addHero(Avenger avenger);
    //public boolean
}
