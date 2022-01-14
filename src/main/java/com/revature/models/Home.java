package com.revature.models;

import java.util.Objects;

public class Home {

    //the below fields match all of the columns fopr the homes table in the databse. We need getters(), seetters() and a toString() method for this class
    private String home_name;
    private String home_str_num;
    private String home_str;
    private String home_city;
    private String home_region;
    private String home_zip;
    private String home_country;
    private String residents;

    public String getHome_name() {
        return home_name;
    }

    public void setHome_name(String home_name) {
        this.home_name = home_name;
    }

    public String getHome_str_num() {
        return home_str_num;
    }

    public void setHome_str_num(String home_str_num) {
        this.home_str_num = home_str_num;
    }

    public String getHome_str() {
        return home_str;
    }

    public void setHome_str(String home_str) {
        this.home_str = home_str;
    }

    public String getHome_city() {
        return home_city;
    }

    public void setHome_city(String home_city) {
        this.home_city = home_city;
    }

    public String getHome_region() {
        return home_region;
    }

    public void setHome_region(String home_region) {
        this.home_region = home_region;
    }

    public String getHome_zip() {
        return home_zip;
    }

    public void setHome_zip(String home_zip) {
        this.home_zip = home_zip;
    }

    public String getHome_country() {
        return home_country;
    }

    public void setHome_country(String home_country) {
        this.home_country = home_country;
    }

    public String getResidents() {
        return residents;
    }

    public void setResidents(String residents) {
        this.residents = residents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Home home = (Home) o;
        return residents == home.residents && Objects.equals(home_name, home.home_name) && Objects.equals(home_str_num, home.home_str_num) && Objects.equals(home_str, home.home_str) && Objects.equals(home_city, home.home_city) && Objects.equals(home_region, home.home_region) && Objects.equals(home_zip, home.home_zip) && Objects.equals(home_country, home.home_country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(home_name, home_str_num, home_str, home_city, home_region, home_zip, home_country, residents);
    }

    @Override
    public String toString() {
        return "Home{" +
                "home_name='" + home_name + '\'' +
                ", home_str_num='" + home_str_num + '\'' +
                ", home_str='" + home_str + '\'' +
                ", home_city='" + home_city + '\'' +
                ", home_region='" + home_region + '\'' +
                ", home_zip='" + home_zip + '\'' +
                ", home_country='" + home_country + '\'' +
                ", residents=" + residents +
                '}';
    }
}
