package com.visualization.data.allyearforcountry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AllYearsForCountryModel {

    public Map<IntermediateAllYears, Integer> getYearToSuicideNo() {
        return yearToSuicideNo;
    }

    public void setYearToSuicideNo(Map<IntermediateAllYears, Integer> yearToSuicideNo) {
        this.yearToSuicideNo = yearToSuicideNo;
    }

    private Map<IntermediateAllYears, Integer> yearToSuicideNo = new HashMap<>();

    public AllYearsForCountryModel put(IntermediateAllYears intermediateAllYears, int suicide_nr) {

        if (this.yearToSuicideNo.containsKey(intermediateAllYears)) {
            this.yearToSuicideNo.put(intermediateAllYears, this.yearToSuicideNo.get(intermediateAllYears) + suicide_nr);
        } else {
            this.yearToSuicideNo.put(intermediateAllYears, suicide_nr);
        }
        return this;
    }

    @Override
    public String toString() {
        return "AllYearsForCountryModel{" +
                "yearToSuicideNo=" + yearToSuicideNo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllYearsForCountryModel that = (AllYearsForCountryModel) o;
        return Objects.equals(yearToSuicideNo, that.yearToSuicideNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(yearToSuicideNo);
    }
}
