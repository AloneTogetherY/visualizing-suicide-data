package com.visualization.data.allyearforcountry;

import java.util.Objects;

public class IntermediateAllYears {

    private String country;
    private int year;
    private double gpd;

    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntermediateAllYears that = (IntermediateAllYears) o;
        return year == that.year &&
                Double.compare(that.gpd, gpd) == 0 &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, year, gpd);
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getGpd() {
        return gpd;
    }

    public void setGpd(double gpd) {
        this.gpd = gpd;
    }

    @Override
    public String toString() {
        return "IntermediateAllYears{" +
                "country='" + country + '\'' +
                ", year=" + year +
                ", gpd=" + gpd +
                '}';
    }

    public IntermediateAllYears(String country, int year, double gpd) {
        this.country = country;
        this.year = year;
        this.gpd = gpd;
    }
}
