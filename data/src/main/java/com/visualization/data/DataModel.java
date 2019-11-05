package com.visualization.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class DataModel {

    private String country, age, generation, sex, country_year, gdp_for_year, region;

    @JsonIgnore
    private String hdi_per_year;

    private int year;
    private int suicides_no;
    private int population;
    private int gdp_per_capita;

    public String getRegion() {
        return region;
    }

    public DataModel region(String region) {
        this.region = region;
        return this;
    }

    public double getArea() {
        return area;
    }

    public DataModel area(double area) {
        this.area = area;
        return this;
    }

    private double area;

    private double suicides_per_100k_population;

    public String getCountry() {
        return country;
    }

    public DataModel country(String country) {
        this.country = country;
        return this;
    }

    public String getAge() {
        return age;
    }

    public DataModel age(String age) {
        if (age == null) this.age = "not available";
        this.age = age;
        return this;

    }

    public String getGeneration() {
        return generation;
    }

    public DataModel generation(String generation) {
        this.generation = generation;
        return this;

    }

    public String getSex() {
        return sex;
    }

    public DataModel sex(String sex) {
        this.sex = sex;
        return this;

    }

    public String getCountry_year() {
        return country_year;
    }

    public DataModel country_year(String country_year) {
        this.country_year = country_year;
        return this;

    }

    public int getYear() {
        return year;
    }

    public DataModel year(int year) {
        this.year = year;
        return this;

    }

    public int getSuicides_no() {
        return suicides_no;
    }

    public DataModel suicides_no(int suicides_no) {
        this.suicides_no = suicides_no;
        return this;

    }

    public int getPopulation() {
        return population;
    }

    public DataModel population(int population) {
        this.population = population;
        return this;

    }

    public int getGdp_per_capita() {
        return gdp_per_capita;
    }

    public DataModel gdp_per_capita(int gdp_per_capita) {
        this.gdp_per_capita = gdp_per_capita;
        return this;

    }

    public String getHdi_per_year() {
        return hdi_per_year;
    }

    public DataModel hdi_per_year(String hdi_per_year) {
        this.hdi_per_year = hdi_per_year;
        return this;

    }

    public double getSuicides_per_100k_population() {
        return suicides_per_100k_population;
    }

    public DataModel suicides_per_100k_population(double suicides_per_100k_population) {
        this.suicides_per_100k_population = suicides_per_100k_population;
        return this;

    }

    public String getGdp_for_year() {
        return gdp_for_year;
    }

    public DataModel gdp_for_year(String gdp_for_year) {
        this.gdp_for_year = gdp_for_year;
        return this;

    }

    public DataModel create() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataModel dataModel = (DataModel) o;
        return year == dataModel.year &&
                suicides_no == dataModel.suicides_no &&
                population == dataModel.population &&
                gdp_per_capita == dataModel.gdp_per_capita &&
                Double.compare(dataModel.suicides_per_100k_population, suicides_per_100k_population) == 0 &&
                Objects.equals(gdp_for_year, dataModel.gdp_for_year) &&
                Objects.equals(country, dataModel.country) &&
                Objects.equals(age, dataModel.age) &&
                Objects.equals(generation, dataModel.generation) &&
                Objects.equals(sex, dataModel.sex) &&
                Objects.equals(hdi_per_year, dataModel.hdi_per_year) &&
                Objects.equals(country_year, dataModel.country_year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, age, generation, sex, country_year, year, suicides_no, population, gdp_per_capita, hdi_per_year, suicides_per_100k_population, gdp_for_year);
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "country='" + country + '\'' +
                ", age='" + age + '\'' +
                ", generation='" + generation + '\'' +
                ", sex='" + sex + '\'' +
                ", country_year='" + country_year + '\'' +
                ", year=" + year +
                ", suicides_no=" + suicides_no +
                ", population=" + population +
                ", gdp_per_capita=" + gdp_per_capita +
                ", hdi_per_year=" + hdi_per_year +
                ", suicides_per_100k_population=" + suicides_per_100k_population +
                ", gdp_for_year=" + gdp_for_year +
                '}';
    }
}
