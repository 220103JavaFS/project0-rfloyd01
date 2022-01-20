package com.revature.repos;

import com.revature.models.Avenger;

public interface AvengerDAO {

    Avenger findHero(int id);
    boolean addHero(Avenger avenger);
}
