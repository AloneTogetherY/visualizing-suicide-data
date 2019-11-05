package com.visualization.data.ageforcountry;

import java.util.Objects;

public class Age {
    private String age;
    private int year;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getYear() {
        return year;
    }

    public Age(String age, int year) {
        this.age = age;
        this.year = year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Age that = (Age) o;
        return year == that.year &&
                Objects.equals(age, that.age);
    }

    @Override
    public String toString() {
        return "Age{" +
                "age='" + age + '\'' +
                ", year=" + year +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, year);
    }

}
