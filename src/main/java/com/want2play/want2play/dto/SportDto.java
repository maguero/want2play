package com.want2play.want2play.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SportDto {

    @NotNull
    private String id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private Integer playersByTeam;

    public SportDto() {
    }

    public SportDto(String id, String name, Integer playersByTeam) {
        this.id = id;
        this.name = name;
        this.playersByTeam = playersByTeam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlayersByTeam() {
        return playersByTeam;
    }

    public void setPlayersByTeam(Integer playersByTeam) {
        this.playersByTeam = playersByTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportDto sportDto = (SportDto) o;
        return Objects.equals(id, sportDto.id) &&
                Objects.equals(name, sportDto.name) &&
                Objects.equals(playersByTeam, sportDto.playersByTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, playersByTeam);
    }

}
