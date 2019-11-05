package com.visualization.data.malefemaleforcountry;

import java.util.Objects;

public class Sex {

    private double suicides;
    private int year;

    public Sex(int year, double suicides) {
        this.year = year;
        this.suicides = suicides;
    }

    public double getSuicides() {
        return suicides;
    }

    @Override
    public String toString() {
        return "Sex{" +
                "suicides=" + suicides +
                ", year=" + year +
                '}';
    }

    public void setSuicides(double suicides) {
        this.suicides = suicides;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sex sex = (Sex) o;
        return Double.compare(sex.suicides, suicides) == 0 &&
                year == sex.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suicides, year);
    }
}
