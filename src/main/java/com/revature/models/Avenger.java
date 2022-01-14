package com.revature.models;

import java.util.Objects;

public class Avenger {

    private int id;
    private String firstName;
    private String lastName;
    private int powerLevel;
    private HeroName heroName;
    private Home home;

    public Avenger() {
    }

    public Avenger(int id, String firstName, String lastName, int powerLevel, HeroName heroName, Home home) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.powerLevel = powerLevel;
        this.heroName = heroName;
        this.home = home;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public HeroName getHeroName() {
        return heroName;
    }

    public void setHeroName(HeroName heroName) {
        this.heroName = heroName;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avenger avenger = (Avenger) o;
        return id == avenger.id && powerLevel == avenger.powerLevel && Objects.equals(firstName, avenger.firstName) && Objects.equals(lastName, avenger.lastName) && Objects.equals(heroName, avenger.heroName) && Objects.equals(home, avenger.home);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, powerLevel, heroName, home);
    }

    @Override
    public String toString() {
        return "Avenger{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", powerLevel=" + powerLevel +
                ", heroName=" + heroName +
                ", home=" + home +
                '}';
    }
}
