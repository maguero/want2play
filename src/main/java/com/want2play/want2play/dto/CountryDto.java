package com.want2play.want2play.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CountryDto {

    @NotNull
    private String code;
    @NotNull
    private String name;
    private List<StateDto> states;

    public CountryDto() {
        this.states = new ArrayList<>();
    }

    public CountryDto(String code, String name) {
        this.code = code;
        this.name = name;
        this.states = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StateDto> getStates() {
        return states;
    }

    public void setStates(List<StateDto> states) {
        this.states = states;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryDto that = (CountryDto) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(states, that.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, states);
    }

    public static class Builder {
        private CountryDto country;
        private String code;
        private String name;
        private List<StateDto> states;

        public Builder() {
            this.country = new CountryDto(null, null);
            this.states = new ArrayList<>();
        }

        public Builder withCode(String code) {
            this.code = code;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withState(StateDto state, List<CityDto> cities) {
            state.getCities().addAll(cities);
            this.states.add(state);
            return this;
        }

        public CountryDto build() {
            country.setCode(this.code);
            country.setName(this.name);
            country.setStates(this.states);
            return country;
        }

    }

}
