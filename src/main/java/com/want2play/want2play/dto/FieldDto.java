package com.want2play.want2play.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class FieldDto {

    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private SportDto sport;

    public FieldDto() {
    }

    public FieldDto(@NotNull @NotEmpty String name, @NotNull SportDto sport) {
        this.name = name;
        this.sport = sport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SportDto getSport() {
        return sport;
    }

    public void setSport(SportDto sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDto fieldDto = (FieldDto) o;
        return Objects.equals(name, fieldDto.name) &&
                Objects.equals(sport, fieldDto.sport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sport);
    }
}
