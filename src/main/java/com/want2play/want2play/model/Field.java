package com.want2play.want2play.model;

import java.util.Objects;

public class Field {

    private String name;
    private String sport;

    public Field(String name, String sport) {
        this.name = name;
        this.sport = sport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
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
