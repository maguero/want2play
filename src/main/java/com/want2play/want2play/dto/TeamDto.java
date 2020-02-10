package com.want2play.want2play.dto;

import java.util.ArrayList;
import java.util.List;

public class TeamDto {

    private int numberOfPlayers;
    private List<PlayerDto> players;

    public TeamDto() {
        this.players = new ArrayList<>();
    }

    public TeamDto(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<>();
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDto> players) {
        this.players = players;
    }
}
