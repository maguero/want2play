package com.want2play.want2play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Document(collection = "matches")
public class Match {

    @Id
    private String id;
    @NotNull
    private Stadium stadium;
    @NotNull
    private Sport sport;
    @NotNull
    private Player adminPlayer;
    private Team teamA;
    private Team teamB;
    @NotNull
    private Date schedule;
    private String notes;
    private MatchStates state;

    public Match() {
    }

    public Match(String id) {
        this.id = id;
        this.state = MatchStates.NEW;
    }

    public Match(Player adminPlayer, MatchStates state) {
        this.id = UUID.randomUUID().toString();
        this.adminPlayer = adminPlayer;
        this.state = state;
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

    public MatchStates getState() {
        return state;
    }

    public void setState(MatchStates state) {
        this.state = state;
    }

    public static class Builder {
        private Match match;
        private String id;
        private Stadium stadium;
        private Sport sport;
        private Player adminPlayer;
        private Team teamA;
        private Team teamB;
        private Date schedule;
        private String notes;
        private MatchStates state;

        public Builder() {
            this.match = new Match(UUID.randomUUID().toString());
            this.teamA = new Team();
            this.teamB = new Team();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withStadium(Stadium stadium) {
            this.stadium = stadium;
            return this;
        }

        public Builder withSport(Sport sport) {
            this.sport = sport;
            this.teamA.setNumberOfPlayers(sport.getPlayersByTeam());
            this.teamB.setNumberOfPlayers(sport.getPlayersByTeam());
            return this;
        }

        public Builder withAdminPlayer(Player player) {
            this.adminPlayer = player;
            return this;
        }

        public Builder withTeamAPlayers(List<Player> players) {
            this.teamA.getPlayers().addAll(players);
            return this;
        }

        public Builder withTeamBPlayers(List<Player> players) {
            this.teamB.getPlayers().addAll(players);
            return this;
        }

        public Builder withSchedule(Date date) {
            this.schedule = date;
            return this;
        }

        public Builder withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder withState(MatchStates state) {
            this.state = state;
            return this;
        }

        public Match build() {
            if (this.id != null) {
                match.setId(this.id);
            }
            match.setStadium(this.stadium);
            match.setSport(this.sport);
            match.setAdminPlayer(this.adminPlayer);
            match.setTeamA(this.teamA);
            match.setTeamB(this.teamB);
            match.setSchedule(this.schedule);
            match.setNotes(this.notes);
            match.setState(this.state);
            return match;
        }

    }
}
