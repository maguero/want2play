package com.want2play.want2play.dto;

import javax.validation.constraints.NotNull;

public class CityDto {

    @NotNull
    private String name;

    public CityDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CityDto() {
    }

}
