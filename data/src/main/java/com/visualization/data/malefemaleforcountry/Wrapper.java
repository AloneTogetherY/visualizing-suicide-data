package com.visualization.data.malefemaleforcountry;

import java.util.List;
import java.util.Objects;

public class Wrapper {

    private List<Sex> males;

    public List<Sex> getMales() {
        return males;
    }

    public void setMales(List<Sex> males) {
        this.males = males;
    }

    public List<Sex> getFemales() {
        return females;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wrapper wrapper = (Wrapper) o;
        return Objects.equals(males, wrapper.males) &&
                Objects.equals(females, wrapper.females);
    }

    @Override
    public String toString() {
        return "Wrapper{" +
                "males=" + males +
                ", females=" + females +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(males, females);
    }

    public void setFemales(List<Sex> females) {
        this.females = females;
    }

    private List<Sex> females;

    public Wrapper(List<Sex> males, List<Sex> females) {
        this.males = males;
        this.females = females;
    }
}
