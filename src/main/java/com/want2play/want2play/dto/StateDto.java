package com.want2play.want2play.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class StateDto {

    @NotNull
    private String name;
    private List<CityDto> cities;

    public StateDto() {
    }

    public StateDto(String name) {
        this.name = name;
        this.cities = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityDto> getCities() {
        return cities;
    }

    public void setCities(List<CityDto> cities) {
        this.cities = cities;
    }

}
