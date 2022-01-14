package com.revature.models;

import java.util.Objects;

public class HeroName {

    private String heroName;
    private String powerDescription;

    public HeroName() {
    }

    public HeroName(String heroName, String powerDexcription) {
        this.heroName = heroName;
        this.powerDescription = powerDexcription;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getPowerDexcription() {
        return powerDescription;
    }

    public void setPowerDexcription(String powerDexcription) {
        this.powerDescription = powerDexcription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeroName heroName1 = (HeroName) o;
        return Objects.equals(heroName, heroName1.heroName) && Objects.equals(powerDescription, heroName1.powerDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heroName, powerDescription);
    }

    @Override
    public String toString() {
        return "HeroName{" +
                "heroName='" + heroName + '\'' +
                ", powerDescription='" + powerDescription + '\'' +
                '}';
    }
}
