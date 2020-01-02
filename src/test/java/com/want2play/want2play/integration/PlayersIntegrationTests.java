package com.want2play.want2play.integration;

import com.want2play.want2play.model.Player;
import com.want2play.want2play.service.PlayerService;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayersIntegrationTests extends AbstractIntegrationTest {

    @Autowired
    private PlayerService playerService;

    private Player insertPlayer(Player player) {
        return playerService.insertPlayer(player);
    }

    @BeforeEach
    public void cleanPlayers() {
        playerService.getAllPlayers().forEach(player -> playerService.deletePlayer(player.getId()));
    }

    @Nested
    class SaveUpdateAndDeletePlayers {

        @Test
        @DisplayName("Insert a player")
        public void insertAPlayer() {
            // given
            Player expectedPlayer = new Player("john.doe@w2p.com", "John Doe");

            // when
            ResponseEntity<Player> actualPlayer = restTemplate.postForEntity("/players", expectedPlayer, Player.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CREATED);
            assertThat(actualPlayer.getBody()).extracting(Player::getId).isEqualTo("john.doe@w2p.com");
            assertThat(actualPlayer.getBody()).extracting(Player::getName).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("Error inserting a non complete player")
        public void insertNonCompletePlayer() {
            // given
            Player expectedPlayer = new Player("john.doe@w2p.com", null);

            // when
            ResponseEntity<Player> actualPlayer = restTemplate.postForEntity("/players", expectedPlayer, Player.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Error inserting a duplicate player")
        public void insertDuplicatePlayer() {
            // given
            insertPlayer(new Player("john.doe@w2p.com", "John Doe"));

            // when
            Player expectedPlayer = new Player("john.doe@w2p.com", "John Doe");
            ResponseEntity<Player> actualPlayer = restTemplate.postForEntity("/players", expectedPlayer, Player.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("Update a player")
        public void updatePlayer() {
            // given
            Player expectedPlayer = insertPlayer(new Player("john.doe@w2p.com", "John Doe"));

            // when
            expectedPlayer.setName("new Name");
            ResponseEntity<Player> actualPlayer = restTemplate.exchange("/players", HttpMethod.PUT, new HttpEntity<>(expectedPlayer), Player.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualPlayer.getBody()).extracting(Player::getName).isEqualTo("new Name");
        }

        @Test
        @DisplayName("Error updating a player")
        public void updateNonCompletePlayer() {
            // given
            Player expectedPlayer = insertPlayer(new Player("john.doe@w2p.com", "John Doe"));

            // when
            expectedPlayer.setName(null);
            ResponseEntity<Player> actualPlayer = restTemplate.exchange("/players", HttpMethod.PUT, new HttpEntity<>(expectedPlayer), Player.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Delete a player")
        public void deletePlayer() {
            // given
            insertPlayer(new Player("john.doe@w2p.com", "John Doe"));

            // when
            ResponseEntity<Player> actualPlayer = restTemplate.exchange("/players/john.doe@w2p.com", HttpMethod.DELETE, new HttpEntity<>(null), Player.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Error deleting a player")
        public void deleteNonExistingPlayer() {
            // when
            ResponseEntity<Player> actualPlayer = restTemplate.exchange("/players/InvalidPlayerID", HttpMethod.DELETE, new HttpEntity<>(null), Player.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

    }

    @Nested
    class GetPlayers {

        @Test
        @DisplayName("Get all players")
        public void getAllPlayers() {
            // given
            insertPlayer(new Player("john.doe@w2p.com", "John Doe"));
            insertPlayer(new Player("johnny.depp@w2p.com", "Johnny Depp"));

            // when
            ResponseEntity<Player[]> players = restTemplate.getForEntity("/players", Player[].class);

            // then
            assertThat(players).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(players.getBody()).hasSize(2);
            List<String> playersIds = Arrays.stream(players.getBody()).map(Player::getId).collect(Collectors.toList());
            assertThat(playersIds).containsAll(Arrays.asList("john.doe@w2p.com", "johnny.depp@w2p.com"));
        }

        @Test
        @DisplayName("Get a player by ID")
        public void getPlayerById() {
            // given
            insertPlayer(new Player("john.doe@w2p.com", "John Doe"));
            insertPlayer(new Player("johnny.depp@w2p.com", "Johnny Depp"));

            // when
            ResponseEntity<Player> actualPlayer = restTemplate.getForEntity("/players/john.doe@w2p.com", Player.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualPlayer.getBody()).extracting(Player::getId).isEqualTo("john.doe@w2p.com");
        }

        @Test
        @DisplayName("Error getting a player by ID")
        public void getNonExistingPlayerById() {
            // when
            ResponseEntity<Player> actualPlayer = restTemplate.getForEntity("/players/invalidId", Player.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Get players by Name")
        public void getPlayersByName() {
            // given
            insertPlayer(new Player("john.doe@w2p.com", "John Doe"));
            insertPlayer(new Player("johnny.depp@w2p.com", "Johnny Depp"));
            insertPlayer(new Player("michael.jordan@w2p.com", "Michael Jordan"));

            // when
            ResponseEntity<Player[]> actualPlayers = restTemplate.getForEntity("/players/?name=John", Player[].class);

            // then
            assertThat(actualPlayers).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            List<String> playersNames = Arrays.stream(actualPlayers.getBody()).map(Player::getName).collect(Collectors.toList());
            assertThat(playersNames).containsAll(Arrays.asList("John Doe", "Johnny Depp"));
        }

    }

}
