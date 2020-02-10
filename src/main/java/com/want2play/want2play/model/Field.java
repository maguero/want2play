package com.want2play.want2play.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Field {

    @NotNull
    private String name;
    @NotNull
    private Sport sport;

    public Field() {
    }

    public Field(String name, Sport sport) {
        this.name = name;
        this.sport = sport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name) &&
                Objects.equals(sport, field.sport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sport);
    }
}
