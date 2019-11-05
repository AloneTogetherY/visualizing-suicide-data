package com.visualization.data.yearcountryquery;

import java.util.HashMap;
import java.util.Map;

public class SuicidesForYearAndCountryIntermediate {

    private Map<PersonalData, Double> ageToSuicidesMap = new HashMap<>();

    public void putInAgeToSuicidesMap(PersonalData personalData, Double suicide_nr) {
        if (this.ageToSuicidesMap.containsKey(personalData)) {
            this.ageToSuicidesMap.put(personalData, this.ageToSuicidesMap.get(personalData) + suicide_nr);
        } else {
            this.ageToSuicidesMap.put(personalData, suicide_nr);
        }
    }

    public Map<PersonalData, Double> getAgeToSuicidesMap() {
        return ageToSuicidesMap;
    }

    public void setAgeToSuicidesMap(Map<PersonalData, Double> ageToSuicidesMap) {
        this.ageToSuicidesMap = ageToSuicidesMap;
    }

    @Override
    public java.lang.String toString() {
        return "SuicidesForYearAndCountryIntermediate{" +
                "ageToSuicidesMap=" + ageToSuicidesMap +
                '}';
    }
}
