package com.want2play.want2play.model;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class State {

    @NotNull
    private String name;
    private List<City> cities;

    public State() {
    }

    public State(String name) {
        this.name = name;
        this.cities = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

}
