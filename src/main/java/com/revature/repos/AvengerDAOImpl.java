package com.revature.repos;

import com.revature.models.Avenger;
import com.revature.models.Home;
import com.revature.models.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AvengerDAOImpl implements AvengerDAO {

    private HomeDAO homeDAO = new HomeDAOImpl();

    @Override
    public Avenger findHero(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM avengers WHERE hero_id = " + id + ";"; //don't need statement protection because id can only be an integer
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            Avenger avenger = new Avenger();

            while(result.next()) {
                avenger.setId(result.getInt("hero_id"));
                avenger.setFirstName(result.getString("first_name"));
                avenger.setLastName(result.getString("last_name"));
                avenger.setPowerLevel(result.getInt("power_level"));

                //Home is based off of a class and not a built-in
                String homeName = result.getString("home_name");
                if (homeName != null) {
                    Home home = homeDAO.findByName(homeName);
                    avenger.setHome(home);
                }
            }
            return avenger;

        } catch (SQLException e) {
            return new Avenger();
        }
    }

    @Override
    public boolean addHero(Avenger avenger) {
        return false;
    }
}
