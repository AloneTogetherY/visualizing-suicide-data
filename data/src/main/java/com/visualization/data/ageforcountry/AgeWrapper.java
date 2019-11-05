package com.visualization.data.ageforcountry;

import java.util.Objects;

public class AgeWrapper {
    private double suicides;
    private String age;

    public double getSuicides() {
        return suicides;
    }

    public void setSuicides(double suicides) {
        this.suicides = suicides;
    }

    public AgeWrapper(double suicides, String age) {
        this.suicides = suicides;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgeWrapper that = (AgeWrapper) o;
        return
                Double.compare(that.suicides, suicides) == 0 &&
                        Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suicides, age);
    }

    public String getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "AgeWrapperForYear{" +
                ", suicides=" + suicides +
                ", age='" + age + '\'' +
                '}';
    }

    public void setAge(String age) {
        this.age = age;
    }
}
