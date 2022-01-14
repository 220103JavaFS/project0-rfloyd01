package com.revature.repos;

import com.revature.models.Home;
import com.revature.models.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HomeDAOImpl implements HomeDAO {

    @Override
    public List<Home> findAll() {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM homes;";

            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql); //doesn't know what object it holds, just pulls info from the db

            List<Home> list = new ArrayList<>(); //don't make this a class variable, we want to make sure we always pull fresh info from the DB

            //result.next() is the same thing as iterator.hasNext(), but it also increments. So more like a combo of iterator.next() and hasNext()
            //starts at -1, so the first call moves it to the first entry
            while (result.next()) {
                Home home = new Home();
                home.setHome_name(result.getString("home_name"));
                home.setHome_str_num(result.getString("home_str_num"));
                home.setHome_str(result.getString("home_str"));
                home.setHome_city(result.getString("home_city"));
                home.setHome_region(result.getString("home_region"));
                home.setHome_zip(result.getString("home_zip"));
                home.setHome_country(result.getString("home_country"));
                home.setResidents(result.getString("residents"));
                list.add(home);
            }
            return list;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Home findByName(String name) {
        return null;
    }

    @Override
    public boolean updateHome(Home home) {
        return false;
    }

    @Override
    public boolean addHome(Home home) {
        return false;
    }
}
