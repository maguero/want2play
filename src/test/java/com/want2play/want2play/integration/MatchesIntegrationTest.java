package com.want2play.want2play.integration;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.model.*;
import com.want2play.want2play.service.MatchService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatchesIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MatchService matchService;

    private Match insertMatch(Match match) {
        return matchService.saveMatch(match);
    }

    private void insertMatches(Match... matches) {
        Arrays.stream(matches).forEach(match -> insertMatch(match));
    }

    @BeforeEach
    private void cleanMatches() {
        matchService.getAllMatches().stream().forEach(match -> matchService.deleteMatch(match.getId()));
    }

    @Nested
    class SavingAndDeletingMatches {
        @Test
        @DisplayName("Save a new Match")
        public void saveAnInitialMatch() {
            // given
            Stadium stadium = new Stadium("Madison Square Garden", "5th Ave", "New York");
            stadium.getFields().add(new Field("Main Arena", "Basketball"));

            List<Player> teamAPlayers = Arrays.asList(
                    new Player("@1", "Player 1"),
                    new Player("@2", "Player 2"),
                    new Player("@3", "Player 3"),
                    new Player("@4", "Player 4"),
                    new Player("@5", "Player 5"));

            List<Player> teamBPlayers = Arrays.asList(
                    new Player("@6", "Player 6"),
                    new Player("@7", "Player 7"),
                    new Player("@8", "Player 8"),
                    new Player("@9", "Player 9"),
                    new Player("@10", "Player 10"));

            Match expectedMatch = new Match.Builder()
                    .withId("ID000")
                    .withSport(new Sport("BSK", "Basketball", 5))
                    .withStadium(stadium)
                    .withAdminPlayer(new Player("@1", "Player 1"))
                    .withTeamAPlayers(teamAPlayers)
                    .withTeamBPlayers(teamBPlayers)
                    .withNotes("A test expectedMatch in NY")
                    .withSchedule(DateTime.now().toDate())
                    .build();

            // when
            ResponseEntity<Match> savedMatchResponse = restTemplate.postForEntity("/matches", expectedMatch, Match.class);
            assertThat(savedMatchResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            // then
            Match actualMatch = savedMatchResponse.getBody();
            assertThat(actualMatch).isNotNull();
            assertThat(actualMatch).extracting(Match::getId).isEqualTo("ID000");
            assertThat(actualMatch).extracting(Match::getNotes).isEqualTo("A test expectedMatch in NY");
            assertThat(actualMatch).extracting(Match::getState).isEqualTo(MatchStates.NEW);
            assertThat(actualMatch).extracting(Match::getSport).isEqualTo(new Sport("BSK", "Basketball", 5));
            assertThat(actualMatch).extracting(Match::getAdminPlayer).isEqualTo(new Player("@1", "Player 1"));
            assertThat(actualMatch.getTeamA().getPlayers()).hasSize(5);
            assertThat(actualMatch.getTeamB().getPlayers()).hasSize(5);
            assertThat(actualMatch).extracting(Match::getStadium).isEqualTo(stadium);
        }

        @Test
        @DisplayName("Error trying to insert a duplicate Match")
        public void errorSavingExistingMatch() {
            // given
            Match expectedMatch = new Match.Builder()
                    .withSport(new Sport("BSK", "Basketball", 5))
                    .withStadium(new Stadium("Madison Squeare Garden", "10th Ave.", "NY"))
                    .withAdminPlayer(new Player("@1", "Player 1"))
                    .withSchedule(DateTime.now().toDate())
                    .build();
            ;
            insertMatch(expectedMatch);

            // when
            ResponseEntity<Match> savedMatchResponse = restTemplate.postForEntity("/matches", expectedMatch, Match.class);
            assertThat(savedMatchResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

            // then
            assertThrows(W2PEntityExistsException.class, () -> {
                matchService.saveMatch(expectedMatch);
            });
        }

        @Test
        @DisplayName("Delete a Match")
        public void deleteAMatch() {
            // given
            Match expectedMatch = new Match.Builder()
                    .withId("ID001")
                    .withSport(new Sport("BSK", "Basketball", 5))
                    .withStadium(new Stadium("Madison Squeare Garden", "10th Ave.", "NY"))
                    .withAdminPlayer(new Player("@1", "Player 1"))
                    .withSchedule(DateTime.now().toDate())
                    .build();
            ;
            insertMatch(expectedMatch);

            // when
            restTemplate.delete("/matches/ID001");
            ResponseEntity<Match> deletedMatch = restTemplate.getForEntity("/matches/ID001", Match.class);

            // then
            assertThat(deletedMatch.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class GettingMatches {

        @Test
        @DisplayName("Return a Match by ID")
        public void returnMatchById() {
            // given
            insertMatch(new Match("ID001"));

            // when
            ResponseEntity<Match> matchesResponse = restTemplate.getForEntity("/matches/ID001", Match.class);

            // then
            assertThat(matchesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(matchesResponse.getBody()).extracting(Match::getId).isEqualTo("ID001");
        }

        @Test
        @DisplayName("Return the Matches created by a player")
        public void returnsMatchesByAdminPlayer() {

            // given
            Match firstMatch = new Match("ID002");
            firstMatch.setAdminPlayer(new Player("john.doe@want2play.com", "John Doe"));
            insertMatch(firstMatch);
            Match secondMatch = new Match("ID003");
            secondMatch.setAdminPlayer(new Player("michael.doe@want2play.com", "Michael Doe"));
            insertMatch(secondMatch);
            Match thirdMatch = new Match("ID004");
            thirdMatch.setAdminPlayer(new Player("john.doe@want2play.com", "John Doe"));
            insertMatch(thirdMatch);

            // when
            ResponseEntity<Match[]> matchesResponse = restTemplate.getForEntity("/matches/?adminPlayer=john.doe@want2play.com", Match[].class);

            // then
            assertThat(matchesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(matchesResponse.getBody()).isNotEmpty();
            assertThat(matchesResponse.getBody()).hasSize(2);
            assertThat(matchesResponse.getBody()[0]).extracting(Match::getId).isEqualTo("ID002");
            assertThat(matchesResponse.getBody()[1]).extracting(Match::getId).isEqualTo("ID004");
        }

        @Test
        @DisplayName("Return Matches by state")
        public void returnsMatchesByState() {

            // given
            Match firstMatch = new Match("ID005");
            firstMatch.setState(MatchStates.FULL);
            insertMatch(firstMatch);
            Match secondMatch = new Match("ID006");
            secondMatch.setState(MatchStates.CANCELLED);
            insertMatch(secondMatch);

            // when
            ResponseEntity<Match[]> matchesResponse = restTemplate.getForEntity("/matches/?state=CANCELLED", Match[].class);

            // then
            assertThat(matchesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(matchesResponse.getBody()).isNotEmpty();
            assertThat(matchesResponse.getBody()).hasSize(1);
            assertThat(matchesResponse.getBody()[0]).extracting(Match::getId).isEqualTo("ID006");
        }

        @Test
        @DisplayName("Return Matches by city and sport")
        public void returnsMatchesByCityAndSport() {

            // given
            Match firstMatch = new Match("ID007");
            firstMatch.setState(MatchStates.FULL);
            firstMatch.setSport(new Sport("soccer", "Soccer", 11));
            firstMatch.setStadium(new Stadium("Joan Miro", null, "Barcelona"));
            insertMatch(firstMatch);
            Match secondMatch = new Match("ID008");
            secondMatch.setState(MatchStates.OPEN);
            secondMatch.setSport(new Sport("basket", "Basketball", 5));
            secondMatch.setStadium(new Stadium("The Stadium", null, "Barcelona"));
            insertMatch(secondMatch);

            // when
            ResponseEntity<Match[]> matchesResponse = restTemplate.getForEntity("/matches/?city=Barcelona&sport=basket", Match[].class);

            // then
            assertThat(matchesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(matchesResponse.getBody()).isNotEmpty();
            assertThat(matchesResponse.getBody()).hasSize(1);
            assertThat(matchesResponse.getBody()[0]).extracting(Match::getId).isEqualTo("ID008");
        }
    }

}
