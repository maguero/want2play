package com.want2play.want2play.model;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Team {

    @NotNull
    private int numberOfPlayers;
    private List<Player> players;

    public Team() {
        this.players = new ArrayList<>();
    }

    public Team(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<>();
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
