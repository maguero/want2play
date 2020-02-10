package com.want2play.want2play.integration;

import com.want2play.want2play.dto.FieldDto;
import com.want2play.want2play.dto.MatchDto;
import com.want2play.want2play.dto.PlayerDto;
import com.want2play.want2play.dto.SportDto;
import com.want2play.want2play.dto.StadiumDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.model.MatchStates;
import com.want2play.want2play.service.MatchService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatchesIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MatchService matchService;

    private MatchDto insertMatch(MatchDto match) throws W2PEntityExistsException {
        return matchService.insertMatch(match);
    }

    @BeforeEach
    public void cleanMatches() {
        matchService.getAllMatches().stream().forEach(match -> {
            try {
                matchService.deleteMatch(match.getId());
            } catch (W2PEntityNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Nested
    class SavingAndDeletingMatches {
        @Test
        @DisplayName("Save a new Match")
        public void saveAnInitialMatch() {
            // given
            StadiumDto stadium = new StadiumDto("Madison Square Garden", "5th Ave", "New York");
            stadium.getFields().add(new FieldDto("Main Arena", new SportDto("BSK", "Basketball", 5)));

            List<PlayerDto> teamAPlayers = Arrays.asList(
                    new PlayerDto("@1", "Player 1"),
                    new PlayerDto("@2", "Player 2"),
                    new PlayerDto("@3", "Player 3"),
                    new PlayerDto("@4", "Player 4"),
                    new PlayerDto("@5", "Player 5"));

            List<PlayerDto> teamBPlayers = Arrays.asList(
                    new PlayerDto("@6", "Player 6"),
                    new PlayerDto("@7", "Player 7"),
                    new PlayerDto("@8", "Player 8"),
                    new PlayerDto("@9", "Player 9"),
                    new PlayerDto("@10", "Player 10"));

            MatchDto expectedMatch = new MatchDto.Builder()
                    .withId("ID000")
                    .withSport(new SportDto("BSK", "Basketball", 5))
                    .withStadium(stadium)
                    .withAdminPlayer(new PlayerDto("@1", "Player 1"))
                    .withTeamAPlayers(teamAPlayers)
                    .withTeamBPlayers(teamBPlayers)
                    .withNotes("A test expectedMatch in NY")
                    .withSchedule(DateTime.now().toDate())
                    .build();

            // when
            ResponseEntity<MatchDto> savedMatchResponse = restTemplate.postForEntity("/matches", expectedMatch, MatchDto.class);
            assertThat(savedMatchResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            // then
            MatchDto actualMatch = savedMatchResponse.getBody();
            assertThat(actualMatch).isNotNull();
            assertThat(actualMatch).extracting(MatchDto::getId).isEqualTo("ID000");
            assertThat(actualMatch).extracting(MatchDto::getNotes).isEqualTo("A test expectedMatch in NY");
            assertThat(actualMatch).extracting(MatchDto::getState).isEqualTo(MatchStates.NEW);
            assertThat(actualMatch).extracting(MatchDto::getSport).isEqualTo(new SportDto("BSK", "Basketball", 5));
            assertThat(actualMatch).extracting(MatchDto::getAdminPlayer).isEqualTo(new PlayerDto("@1", "Player 1"));
            assertThat(actualMatch.getTeamA().getPlayers()).hasSize(5);
            assertThat(actualMatch.getTeamB().getPlayers()).hasSize(5);
            assertThat(actualMatch).extracting(MatchDto::getStadium).isEqualTo(stadium);
        }

        @Test
        @DisplayName("Error trying to insert a duplicate Match")
        public void errorSavingExistingMatch() throws W2PEntityExistsException {
            // given
            MatchDto expectedMatch = new MatchDto.Builder()
                    .withSport(new SportDto("BSK", "Basketball", 5))
                    .withStadium(new StadiumDto("Madison Squeare Garden", "10th Ave.", "NY"))
                    .withAdminPlayer(new PlayerDto("@1", "Player 1"))
                    .withSchedule(DateTime.now().toDate())
                    .build();
            insertMatch(expectedMatch);

            // when
            ResponseEntity<MatchDto> savedMatchResponse = restTemplate.postForEntity("/matches", expectedMatch, MatchDto.class);
            assertThat(savedMatchResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

            // then
            assertThrows(W2PEntityExistsException.class, () -> {
                matchService.insertMatch(expectedMatch);
            });
        }

        @Test
        @DisplayName("Delete a Match")
        public void deleteAMatch() throws W2PEntityExistsException {
            // given
            MatchDto expectedMatch = new MatchDto.Builder()
                    .withId("ID001")
                    .withSport(new SportDto("BSK", "Basketball", 5))
                    .withStadium(new StadiumDto("Madison Squeare Garden", "10th Ave.", "NY"))
                    .withAdminPlayer(new PlayerDto("@1", "Player 1"))
                    .withSchedule(DateTime.now().toDate())
                    .build();
            insertMatch(expectedMatch);

            // when
            restTemplate.delete("/matches/ID001");
            ResponseEntity<MatchDto> deletedMatch = restTemplate.getForEntity("/matches/ID001", MatchDto.class);

            // then
            assertThat(deletedMatch.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class GettingMatches {

        @Test
        @DisplayName("Return a MatchDto by ID")
        public void returnMatchById() throws W2PEntityExistsException {
            // given
            insertMatch(new MatchDto("ID001"));

            // when
            ResponseEntity<MatchDto> matchesResponse = restTemplate.getForEntity("/matches/ID001", MatchDto.class);

            // then
            assertThat(matchesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(matchesResponse.getBody()).extracting(MatchDto::getId).isEqualTo("ID001");
        }

        @Test
        @DisplayName("Return the Matches created by a player")
        public void returnsMatchesByAdminPlayer() throws W2PEntityExistsException {

            // given
            MatchDto firstMatch = new MatchDto("ID002");
            firstMatch.setAdminPlayer(new PlayerDto("john.doe@want2play.com", "John Doe"));
            insertMatch(firstMatch);
            MatchDto secondMatch = new MatchDto("ID003");
            secondMatch.setAdminPlayer(new PlayerDto("michael.doe@want2play.com", "Michael Doe"));
            insertMatch(secondMatch);
            MatchDto thirdMatch = new MatchDto("ID004");
            thirdMatch.setAdminPlayer(new PlayerDto("john.doe@want2play.com", "John Doe"));
            insertMatch(thirdMatch);

            // when
            ResponseEntity<MatchDto[]> matchesResponse = restTemplate.getForEntity("/matches/?adminPlayer=john.doe@want2play.com", MatchDto[].class);

            // then
            assertThat(matchesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(matchesResponse.getBody()).isNotEmpty();
            assertThat(matchesResponse.getBody()).hasSize(2);
            assertThat(matchesResponse.getBody()[0]).extracting(MatchDto::getId).isEqualTo("ID002");
            assertThat(matchesResponse.getBody()[1]).extracting(MatchDto::getId).isEqualTo("ID004");
        }

        @Test
        @DisplayName("Return Matches by state")
        public void returnsMatchesByState() throws W2PEntityExistsException {

            // given
            MatchDto firstMatch = new MatchDto("ID005");
            firstMatch.setState(MatchStates.FULL);
            insertMatch(firstMatch);
            MatchDto secondMatch = new MatchDto("ID006");
            secondMatch.setState(MatchStates.CANCELLED);
            insertMatch(secondMatch);

            // when
            ResponseEntity<MatchDto[]> matchesResponse = restTemplate.getForEntity("/matches/?state=CANCELLED", MatchDto[].class);

            // then
            assertThat(matchesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(matchesResponse.getBody()).isNotEmpty();
            assertThat(matchesResponse.getBody()).hasSize(1);
            assertThat(matchesResponse.getBody()[0]).extracting(MatchDto::getId).isEqualTo("ID006");
        }

        @Test
        @DisplayName("Return Matches by city and sport")
        public void returnsMatchesByCityAndSport() throws W2PEntityExistsException {

            // given
            MatchDto firstMatch = new MatchDto("ID007");
            firstMatch.setState(MatchStates.FULL);
            firstMatch.setSport(new SportDto("soccer", "Soccer", 11));
            firstMatch.setStadium(new StadiumDto("Joan Miro", null, "Barcelona"));
            insertMatch(firstMatch);
            MatchDto secondMatch = new MatchDto("ID008");
            secondMatch.setState(MatchStates.OPEN);
            secondMatch.setSport(new SportDto("basket", "Basketball", 5));
            secondMatch.setStadium(new StadiumDto("The Stadium", null, "Barcelona"));
            insertMatch(secondMatch);

            // when
            ResponseEntity<MatchDto[]> matchesResponse = restTemplate.getForEntity("/matches/?city=Barcelona&sport=basket", MatchDto[].class);

            // then
            assertThat(matchesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(matchesResponse.getBody()).isNotEmpty();
            assertThat(matchesResponse.getBody()).hasSize(1);
            assertThat(matchesResponse.getBody()[0]).extracting(MatchDto::getId).isEqualTo("ID008");
        }
    }

}
