package com.want2play.want2play.integration;

import com.want2play.want2play.model.Sport;
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

    private Sport insertSport(Sport sport) {
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
            Sport expectedSport = new Sport("BSK", "Basketball", 5);

            // when
            ResponseEntity<Sport> actualSport = restTemplate.postForEntity("/sports", expectedSport, Sport.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CREATED);
            assertThat(actualSport.getBody()).extracting(Sport::getId).isEqualTo("BSK");
            assertThat(actualSport.getBody()).extracting(Sport::getName).isEqualTo("Basketball");
            assertThat(actualSport.getBody()).extracting(Sport::getPlayersByTeam).isEqualTo(5);
        }

        @Test
        @DisplayName("Error inserting a non complete sport")
        public void insertNonCompleteSport() {
            // given
            Sport expectedSport = new Sport("BSK", "Basketball", null);

            // when
            ResponseEntity<Sport> actualSport = restTemplate.postForEntity("/sports", expectedSport, Sport.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Error inserting a duplicate sport")
        public void insertDuplicateSport() {
            // given
            insertSport(new Sport("BSK", "Basketball", 5));

            // when
            Sport expectedSport = new Sport("BSK", "Basketball", 5);
            ResponseEntity<Sport> actualSport = restTemplate.postForEntity("/sports", expectedSport, Sport.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("Update a sport")
        public void updateSport() {
            // given
            Sport expectedSport = insertSport(new Sport("BSK", "Basketball", 5));

            // when
            expectedSport.setName("new Name");
            ResponseEntity<Sport> actualSport = restTemplate.exchange("/sports", HttpMethod.PUT, new HttpEntity<>(expectedSport), Sport.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualSport.getBody()).extracting(Sport::getName).isEqualTo("new Name");
        }

        @Test
        @DisplayName("Error updating a sport")
        public void updateNonCompleteSport() {
            // given
            Sport expectedSport = insertSport(new Sport("BSK", "Basketball", 5));

            // when
            expectedSport.setName(null);
            ResponseEntity<Sport> actualSport = restTemplate.exchange("/sports", HttpMethod.PUT, new HttpEntity<>(expectedSport), Sport.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Delete a sport")
        public void deleteSport() {
            // given
            insertSport(new Sport("BSK", "Basketball", 5));

            // when
            ResponseEntity<Sport> actualSport = restTemplate.exchange("/sports/BSK", HttpMethod.DELETE, new HttpEntity<>(null), Sport.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Error deleting a sport")
        public void deleteNonExistingSport() {
            // when
            ResponseEntity<Sport> actualSport = restTemplate.exchange("/sports/InvalidSportID", HttpMethod.DELETE, new HttpEntity<>(null), Sport.class);

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
            insertSport(new Sport("BSK", "Basketball", 5));
            insertSport(new Sport("TNS2", "Tennis - Double", 2));

            // when
            ResponseEntity<Sport[]> sports = restTemplate.getForEntity("/sports", Sport[].class);

            // then
            assertThat(sports).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(sports.getBody()).hasSize(2);
            List<String> sportsIds = Arrays.stream(sports.getBody()).map(Sport::getId).collect(Collectors.toList());
            assertThat(sportsIds).containsAll(Arrays.asList("BSK", "TNS2"));
        }

        @Test
        @DisplayName("Get a sport by ID")
        public void getSportById() {
            // given
            insertSport(new Sport("BSK", "Basketball", 5));
            insertSport(new Sport("TNS2", "Tennis - Double", 2));

            // when
            ResponseEntity<Sport> actualSport = restTemplate.getForEntity("/sports/BSK", Sport.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualSport.getBody()).extracting(Sport::getId).isEqualTo("BSK");
        }

        @Test
        @DisplayName("Error getting a sport by ID")
        public void getNonExistingSportById() {
            // when
            ResponseEntity<Sport> actualSport = restTemplate.getForEntity("/sports/invalidId", Sport.class);

            // then
            assertThat(actualSport).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Get sports by Name")
        public void getSportsByName() {
            // given
            insertSport(new Sport("BSK", "Basketball", 5));
            insertSport(new Sport("TNS2", "Tennis - Double", 2));
            insertSport(new Sport("VLY", "Volleyball", 2));

            // when
            ResponseEntity<Sport[]> actualSports = restTemplate.getForEntity("/sports/?name=ball", Sport[].class);

            // then
            assertThat(actualSports).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            List<String> sportsNames = Arrays.stream(actualSports.getBody()).map(Sport::getName).collect(Collectors.toList());
            assertThat(sportsNames).containsAll(Arrays.asList("Basketball", "Volleyball"));
        }

    }

}
