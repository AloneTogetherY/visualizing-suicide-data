package com.visualization.data.yearcountryquery;

import java.util.HashMap;
import java.util.Map;

public class SuicidesForYearAndCountry {
    private double female, male;

    public Map<String, Double> getAgeGroupToSuicide() {
        return ageGroupToSuicide;
    }

    public void put(String age, double suicide_nr) {

        if (this.ageGroupToSuicide.containsKey(age)) {
            this.ageGroupToSuicide.put(age, this.ageGroupToSuicide.get(age) + suicide_nr);
        } else {
            this.ageGroupToSuicide.put(age, suicide_nr);
        }

    }

    private Map<String, Double> ageGroupToSuicide;

    public double getFemale() {
        return female;
    }

    public void setMaleUndFemale(double male, double female) {
        this.male = male;
        this.female = female;
    }

    public double getMale() {
        return male;
    }

    public SuicidesForYearAndCountry() {
        ageGroupToSuicide = new HashMap<>();
    }

    @Override
    public String toString() {
        return "SuicidesForYearAndCountry{" +
                "female=" + female +
                ", male=" + male +
                '}';
    }

}
