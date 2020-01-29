package com.want2play.want2play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sports")
public class Sport {

    @Id
    private String id;
    private String name;
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

}
