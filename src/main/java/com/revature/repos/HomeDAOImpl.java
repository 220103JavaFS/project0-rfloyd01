package com.revature.repos;

import com.revature.models.Home;
import com.revature.models.util.ConnectionUtil;

import java.sql.*;
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

        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM homes WHERE home_name = ?;"; //the ? here is for use with Prepared statements. This is to make sure a user doesn't pass an SQL statement and mess things up

            PreparedStatement statement = conn.prepareStatement(sql);

            //parameter index indicates the "?" that will be replaced with the value given as the second input. Not 0 indexed here so be careful
            statement.setString(1, name);

            ResultSet result = statement.executeQuery();

            Home home = new Home();

            while (result.next()) {
                home.setHome_name(result.getString("home_name"));
                home.setHome_str_num(result.getString("home_str_num"));
                home.setHome_str(result.getString("home_str"));
                home.setHome_city(result.getString("home_city"));
                home.setHome_region(result.getString("home_region"));
                home.setHome_zip(result.getString("home_zip"));
                home.setHome_country(result.getString("home_country"));
                home.setResidents(result.getString("residents"));
            }

            return home;

        } catch (SQLException e) {
            return new Home();
        }
    }

    @Override
    public boolean updateHome(Home home) {

        try (Connection conn = ConnectionUtil.getConnection()) {
            //since we're updating information with something from a user, we want to use a prepared statement
            String sql = "UPDATE homes SET home_name = ?, home_str_num = ?, home_str = ?, home_city = ?, home_region = ?, home_zip = ?, home_country " +
                    "= ? WHERE home_name = ?"; //don't update residents here as that is covered by our internal trigger
            PreparedStatement statement = conn.prepareStatement(sql);

            //auto incrementing a count variable makes it easier to keep track of things here without needing a loop
            int count = 0;
            statement.setString(++count, home.getHome_name());
            statement.setString(++count, home.getHome_str_num());
            statement.setString(++count, home.getHome_str());
            statement.setString(++count, home.getHome_city());
            statement.setString(++count, home.getHome_region());
            statement.setString(++count, home.getHome_zip());
            statement.setString(++count, home.getHome_country());
            statement.setString(++count, home.getHome_name());

            statement.execute();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean addHome(Home home) {

        //use a try with resources block so that the connection will close when the block is complete.
        try (Connection conn = ConnectionUtil.getConnection()) {
            //since we're updating information with something from a user, we want to use a prepared statement
            String sql = "INSERT INTO homes (home_name, home_str_num, home_str, home_city, home_region, home_zip, home_country) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);"; //don't update residents here as that is covered by our internal trigger
            PreparedStatement statement = conn.prepareStatement(sql);

            //auto incrementing a count variable makes it easier to keep track of things here without needing a loop
            int count = 0;
            statement.setString(++count, home.getHome_name());
            statement.setString(++count, home.getHome_str_num());
            statement.setString(++count, home.getHome_str());
            statement.setString(++count, home.getHome_city());
            statement.setString(++count, home.getHome_region());
            statement.setString(++count, home.getHome_zip());
            statement.setString(++count, home.getHome_country());

            statement.execute();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
