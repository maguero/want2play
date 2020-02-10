package com.want2play.want2play.model;

import javax.validation.constraints.NotNull;

public class City {

    @NotNull
    private String name;

    public City() {
    }

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
