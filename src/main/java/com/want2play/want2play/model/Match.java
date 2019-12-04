package com.want2play.want2play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document(collection = "matches")
public class Match {

    @Id
    private String id;
    private Stadium stadium;
    private Sport sport;
    private Player adminPlayer;
    private Team teamA;
    private Team teamB;
    private Date schedule;
    private String notes;
    private String state;

    public Match() {
    }

    public Match(String id) {
        this.id = id;
        this.state = MatchStates.NEW.toString();
    }

    public Match(Player adminPlayer, MatchStates state) {
        this.id = UUID.randomUUID().toString();
        this.adminPlayer = adminPlayer;
        this.state = state.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Player getAdminPlayer() {
        return adminPlayer;
    }

    public void setAdminPlayer(Player adminPlayer) {
        this.adminPlayer = adminPlayer;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getState() {
        return state;
    }

    public void setState(MatchStates state) {
        this.state = state.toString();
    }
}
