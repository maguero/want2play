package com.want2play.want2play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Document(collection = "sports")
public class Sport {

    @Id
    private String id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private Integer playersByTeam;

    public Sport() {
    }

    public Sport(String id, String name, Integer playersByTeam) {
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
        Sport sport = (Sport) o;
        return playersByTeam == sport.playersByTeam &&
                Objects.equals(id, sport.id) &&
                Objects.equals(name, sport.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, playersByTeam);
    }
}
