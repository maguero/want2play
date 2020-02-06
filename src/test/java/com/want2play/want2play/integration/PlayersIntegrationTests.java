package com.want2play.want2play.integration;

import com.want2play.want2play.dto.PlayerDto;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
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

    private PlayerDto insertPlayer(PlayerDto player) {
        return playerService.insertPlayer(player);

    }

    @BeforeEach
    public void cleanPlayers() {
        playerService.getAllPlayers().forEach(player -> {
            try {
                playerService.deletePlayer(player.getId());
            } catch (W2PEntityNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Nested
    class SaveUpdateAndDeletePlayers {

        @Test
        @DisplayName("Insert a player")
        public void insertAPlayer() {
            // given
            PlayerDto expectedPlayer = new PlayerDto("john.doe@w2p.com", "John Doe");

            // when
            ResponseEntity<PlayerDto> actualPlayer = restTemplate.postForEntity("/players", expectedPlayer, PlayerDto.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CREATED);
            assertThat(actualPlayer.getBody()).extracting(PlayerDto::getId).isEqualTo("john.doe@w2p.com");
            assertThat(actualPlayer.getBody()).extracting(PlayerDto::getName).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("Error inserting a non complete player")
        public void insertNonCompletePlayer() {
            // given
            PlayerDto expectedPlayer = new PlayerDto("john.doe@w2p.com", null);

            // when
            ResponseEntity<PlayerDto> actualPlayer = restTemplate.postForEntity("/players", expectedPlayer, PlayerDto.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Error inserting a duplicate player")
        public void insertDuplicatePlayer() {
            // given
            insertPlayer(new PlayerDto("john.doe@w2p.com", "John Doe"));

            // when
            PlayerDto expectedPlayer = new PlayerDto("john.doe@w2p.com", "John Doe");
            ResponseEntity<PlayerDto> actualPlayer = restTemplate.postForEntity("/players", expectedPlayer, PlayerDto.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("Update a player")
        public void updatePlayer() {
            // given
            insertPlayer(new PlayerDto("john.doe@w2p.com", "John Doe"));

            // when
            ResponseEntity<PlayerDto> actualPlayer = restTemplate.exchange(
                    "/players/john.doe@w2p.com",
                    HttpMethod.PUT,
                    new HttpEntity<>(new PlayerDto.PlayerUpdateDto("new Name")),
                    PlayerDto.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualPlayer.getBody()).extracting(PlayerDto::getName).isEqualTo("new Name");
        }

        @Test
        @DisplayName("Error updating a player")
        public void updateNonCompletePlayer() {
            // given
            PlayerDto expectedPlayer = insertPlayer(new PlayerDto("john.doe@w2p.com", "John Doe"));

            // when
            ResponseEntity<PlayerDto> actualPlayer = restTemplate.exchange(
                    "/players/john.doe@w2p.com",
                    HttpMethod.PUT,
                    new HttpEntity<>(new PlayerDto.PlayerUpdateDto(null)),
                    PlayerDto.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Delete a player")
        public void deletePlayer() {
            // given
            insertPlayer(new PlayerDto("john.doe@w2p.com", "John Doe"));

            // when
            ResponseEntity<PlayerDto> actualPlayer = restTemplate.exchange("/players/john.doe@w2p.com", HttpMethod.DELETE, new HttpEntity<>(null), PlayerDto.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Error deleting a player")
        public void deleteNonExistingPlayer() {
            // when
            ResponseEntity<PlayerDto> actualPlayer = restTemplate.exchange("/players/InvalidPlayerID", HttpMethod.DELETE, new HttpEntity<>(null), PlayerDto.class);

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
            insertPlayer(new PlayerDto("john.doe@w2p.com", "John Doe"));
            insertPlayer(new PlayerDto("johnny.depp@w2p.com", "Johnny Depp"));

            // when
            ResponseEntity<PlayerDto[]> players = restTemplate.getForEntity("/players", PlayerDto[].class);

            // then
            assertThat(players).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(players.getBody()).hasSize(2);
            List<String> playersIds = Arrays.stream(players.getBody()).map(PlayerDto::getId).collect(Collectors.toList());
            assertThat(playersIds).containsAll(Arrays.asList("john.doe@w2p.com", "johnny.depp@w2p.com"));
        }

        @Test
        @DisplayName("Get a player by ID")
        public void getPlayerById() {
            // given
            insertPlayer(new PlayerDto("john.doe@w2p.com", "John Doe"));
            insertPlayer(new PlayerDto("johnny.depp@w2p.com", "Johnny Depp"));

            // when
            ResponseEntity<PlayerDto> actualPlayer = restTemplate.getForEntity("/players/john.doe@w2p.com", PlayerDto.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualPlayer.getBody()).extracting(PlayerDto::getId).isEqualTo("john.doe@w2p.com");
        }

        @Test
        @DisplayName("Error getting a player by ID")
        public void getNonExistingPlayerById() {
            // when
            ResponseEntity<PlayerDto> actualPlayer = restTemplate.getForEntity("/players/invalidId", PlayerDto.class);

            // then
            assertThat(actualPlayer).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Get players by Name")
        public void getPlayersByName() {
            // given
            insertPlayer(new PlayerDto("john.doe@w2p.com", "John Doe"));
            insertPlayer(new PlayerDto("johnny.depp@w2p.com", "Johnny Depp"));
            insertPlayer(new PlayerDto("michael.jordan@w2p.com", "Michael Jordan"));

            // when
            ResponseEntity<PlayerDto[]> actualPlayers = restTemplate.getForEntity("/players/?name=John", PlayerDto[].class);

            // then
            assertThat(actualPlayers).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            List<String> playersNames = Arrays.stream(actualPlayers.getBody()).map(PlayerDto::getName).collect(Collectors.toList());
            assertThat(playersNames).containsAll(Arrays.asList("John Doe", "Johnny Depp"));
        }

    }

}
