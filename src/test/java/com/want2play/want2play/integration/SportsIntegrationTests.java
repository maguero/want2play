package com.want2play.want2play.integration;

import com.want2play.want2play.dto.SportDto;
import com.want2play.want2play.service.SportService;
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

public class SportsIntegrationTests extends AbstractIntegrationTest {

    @Autowired
    private SportService sportService;

    private SportDto insertSport(SportDto sport) {
        return sportService.insertSport(sport);
    }

    @BeforeEach
    public void cleanSports() {
        sportService.getAllSports().forEach(sport -> sportService.deleteSport(sport.getId()));
    }

    @Nested
    class SaveUpdateAndDeleteSports {

        @Test
        @DisplayName("Insert a sport")
        public void insertASport() {
            // given
            SportDto expectedSport = new SportDto("BSK", "Basketball", 5);

            // when
            ResponseEntity<SportDto> actualSport = restTemplate.postForEntity("/sports", expectedSport, SportDto.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CREATED);
            assertThat(actualSport.getBody()).extracting(SportDto::getId).isEqualTo("BSK");
            assertThat(actualSport.getBody()).extracting(SportDto::getName).isEqualTo("Basketball");
            assertThat(actualSport.getBody()).extracting(SportDto::getPlayersByTeam).isEqualTo(5);
        }

        @Test
        @DisplayName("Error inserting a non complete sport")
        public void insertNonCompleteSport() {
            // given
            SportDto expectedSport = new SportDto("BSK", "Basketball", null);

            // when
            ResponseEntity<SportDto> actualSport = restTemplate.postForEntity("/sports", expectedSport, SportDto.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Error inserting a duplicate sport")
        public void insertDuplicateSport() {
            // given
            insertSport(new SportDto("BSK", "Basketball", 5));

            // when
            SportDto expectedSport = new SportDto("BSK", "Basketball", 5);
            ResponseEntity<SportDto> actualSport = restTemplate.postForEntity("/sports", expectedSport, SportDto.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("Update a sport")
        public void updateSport() {
            // given
            SportDto expectedSport = insertSport(new SportDto("BSK", "Basketball", 5));

            // when
            expectedSport.setName("new Name");
            ResponseEntity<SportDto> actualSport = restTemplate.exchange("/sports", HttpMethod.PUT, new HttpEntity<>(expectedSport), SportDto.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualSport.getBody()).extracting(SportDto::getName).isEqualTo("new Name");
        }

        @Test
        @DisplayName("Error updating a sport")
        public void updateNonCompleteSport() {
            // given
            SportDto expectedSport = insertSport(new SportDto("BSK", "Basketball", 5));

            // when
            expectedSport.setName(null);
            ResponseEntity<SportDto> actualSport = restTemplate.exchange("/sports", HttpMethod.PUT, new HttpEntity<>(expectedSport), SportDto.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Delete a sport")
        public void deleteSport() {
            // given
            insertSport(new SportDto("BSK", "Basketball", 5));

            // when
            ResponseEntity<SportDto> actualSport = restTemplate.exchange("/sports/BSK", HttpMethod.DELETE, new HttpEntity<>(null), SportDto.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Error deleting a sport")
        public void deleteNonExistingSport() {
            // when
            ResponseEntity<SportDto> actualSport = restTemplate.exchange("/sports/InvalidSportID", HttpMethod.DELETE, new HttpEntity<>(null), SportDto.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

    }

    @Nested
    class GetSports {

        @Test
        @DisplayName("Get all sports")
        public void getAllSports() {
            // given
            insertSport(new SportDto("BSK", "Basketball", 5));
            insertSport(new SportDto("TNS2", "Tennis - Double", 2));

            // when
            ResponseEntity<SportDto[]> sports = restTemplate.getForEntity("/sports", SportDto[].class);

            // then
            assertThat(sports).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(sports.getBody()).hasSize(2);
            List<String> sportsIds = Arrays.stream(sports.getBody()).map(SportDto::getId).collect(Collectors.toList());
            assertThat(sportsIds).containsAll(Arrays.asList("BSK", "TNS2"));
        }

        @Test
        @DisplayName("Get a sport by ID")
        public void getSportById() {
            // given
            insertSport(new SportDto("BSK", "Basketball", 5));
            insertSport(new SportDto("TNS2", "Tennis - Double", 2));

            // when
            ResponseEntity<SportDto> actualSport = restTemplate.getForEntity("/sports/BSK", SportDto.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualSport.getBody()).extracting(SportDto::getId).isEqualTo("BSK");
        }

        @Test
        @DisplayName("Error getting a sport by ID")
        public void getNonExistingSportById() {
            // when
            ResponseEntity<SportDto> actualSport = restTemplate.getForEntity("/sports/invalidId", SportDto.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Get sports by Name")
        public void getSportsByName() {
            // given
            insertSport(new SportDto("BSK", "Basketball", 5));
            insertSport(new SportDto("TNS2", "Tennis - Double", 2));
            insertSport(new SportDto("VLY", "Volleyball", 2));

            // when
            ResponseEntity<SportDto[]> actualSports = restTemplate.getForEntity("/sports/?name=ball", SportDto[].class);

            // then
            assertThat(actualSports).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            List<String> sportsNames = Arrays.stream(actualSports.getBody()).map(SportDto::getName).collect(Collectors.toList());
            assertThat(sportsNames).containsAll(Arrays.asList("Basketball", "Volleyball"));
        }

    }

}
