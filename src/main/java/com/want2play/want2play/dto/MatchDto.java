package com.want2play.want2play.dto;

import com.want2play.want2play.model.MatchStates;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MatchDto {

    private String id;
    @NotNull
    private StadiumDto stadium;
    @NotNull
    private SportDto sport;
    @NotNull
    private PlayerDto adminPlayer;
    private TeamDto teamA;
    private TeamDto teamB;
    @NotNull
    private Date schedule;
    private String notes;
    private MatchStates state;

    public MatchDto() {
    }

    public MatchDto(String id) {
        this.id = id;
        this.state = MatchStates.NEW;
    }

    public MatchDto(PlayerDto adminPlayer, MatchStates state) {
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

    public StadiumDto getStadium() {
        return stadium;
    }

    public void setStadium(StadiumDto stadium) {
        this.stadium = stadium;
    }

    public SportDto getSport() {
        return sport;
    }

    public void setSport(SportDto sport) {
        this.sport = sport;
    }

    public PlayerDto getAdminPlayer() {
        return adminPlayer;
    }

    public void setAdminPlayer(PlayerDto adminPlayer) {
        this.adminPlayer = adminPlayer;
    }

    public TeamDto getTeamA() {
        return teamA;
    }

    public void setTeamA(TeamDto teamA) {
        this.teamA = teamA;
    }

    public TeamDto getTeamB() {
        return teamB;
    }

    public void setTeamB(TeamDto teamB) {
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
        private MatchDto match;
        private String id;
        private StadiumDto stadium;
        private SportDto sport;
        private PlayerDto adminPlayer;
        private TeamDto teamA;
        private TeamDto teamB;
        private Date schedule;
        private String notes;
        private MatchStates state;

        public Builder() {
            this.match = new MatchDto(UUID.randomUUID().toString());
            this.teamA = new TeamDto();
            this.teamB = new TeamDto();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withStadium(StadiumDto stadium) {
            this.stadium = stadium;
            return this;
        }

        public Builder withSport(SportDto sport) {
            this.sport = sport;
            this.teamA.setNumberOfPlayers(sport.getPlayersByTeam());
            this.teamB.setNumberOfPlayers(sport.getPlayersByTeam());
            return this;
        }

        public Builder withAdminPlayer(PlayerDto player) {
            this.adminPlayer = player;
            return this;
        }

        public Builder withTeamAPlayers(List<PlayerDto> players) {
            this.teamA.getPlayers().addAll(players);
            return this;
        }

        public Builder withTeamBPlayers(List<PlayerDto> players) {
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

        public MatchDto build() {
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
