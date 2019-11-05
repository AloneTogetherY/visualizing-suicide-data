package com.visualization.data.yearquery;

import java.util.List;

public class SuicidePerYear {

    private int suicides_nr;

    private int normalized_value;

    public int getNormalized_value() {
        return normalized_value;
    }

    public void setNormalized_value(int normalized_value) {
        this.normalized_value = normalized_value;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public SuicidePerYear(List<Integer> suicides_nr, String year) {
        suicides_nr.forEach(a ->
                this.suicides_nr += a
        );
        this.year = year;
    }

    private String year;

    public int getSuicides_nr() {
        return suicides_nr;
    }

    public void setSuicides_nr(int suicides_nr) {
        this.suicides_nr += suicides_nr;
    }

    @Override
    public String toString() {
        return "SuicidePerYear{" +
                "suicides_nr=" + suicides_nr +
                ", year='" + year + '\'' +
                '}';
    }
}

