package com.visualization.data.yearcountryquery;

import java.util.Objects;

public class PersonalData {

    private String age, sex;
    private int year;

    public PersonalData(String age, String sex) {
        this.age = age;
        this.sex = sex;
    }

    public PersonalData(String age, String sex, int year) {
        this.age = age;
        this.sex = sex;
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonalData that = (PersonalData) o;
        return year == that.year &&
                Objects.equals(age, that.age) &&
                Objects.equals(sex, that.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, sex, year);
    }

    @Override
    public String toString() {
        return "PersonalData{" +
                "age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                ", year=" + year +
                '}';
    }
}
