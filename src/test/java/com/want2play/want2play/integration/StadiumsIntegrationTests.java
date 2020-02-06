package com.want2play.want2play.integration;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.model.Field;
import com.want2play.want2play.model.Stadium;
import com.want2play.want2play.service.StadiumService;
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

public class StadiumsIntegrationTests extends AbstractIntegrationTest {

    @Autowired
    private StadiumService stadiumService;

    private Stadium insertStadium(Stadium stadium) throws W2PEntityExistsException {
        return stadiumService.insertStadium(stadium);
    }

    @BeforeEach
    public void cleanStadiums() {
        stadiumService.getAllStadiums().forEach(stadium -> {
            try {
                stadiumService.deleteStadium(stadium.getId());
            } catch (W2PEntityNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Nested
    class SaveUpdateAndDeleteStadiums {

        @Test
        @DisplayName("Insert a stadium")
        public void insertAStadium() {
            // given
            List<Field> fields = Arrays.asList(
                    new Field("Camp Nou", "Soccer"),
                    new Field("Palau Baulgrama", "Basketball"));
            Stadium expectedStadium = new Stadium.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .withCity("Barcelona")
                    .withFields(fields)
                    .build();

            // when
            ResponseEntity<Stadium> actualStadium = restTemplate.postForEntity("/stadiums", expectedStadium, Stadium.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CREATED);
            assertThat(actualStadium.getBody()).isEqualTo(expectedStadium);
        }

        @Test
        @DisplayName("Error inserting a non complete stadium")
        public void insertNonCompleteStadium() {
            // given
            Stadium expectedStadium = new Stadium.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .build();

            // when
            ResponseEntity<Stadium> actualStadium = restTemplate.postForEntity("/stadiums", expectedStadium, Stadium.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Error inserting a duplicate stadium")
        public void insertDuplicateStadium() throws W2PEntityExistsException {
            // given
            Stadium expectedStadium = new Stadium.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(new Field("Palau Baulgrama", "Basketball")))
                    .build();
            insertStadium(expectedStadium);

            // when
            ResponseEntity<Stadium> actualStadium = restTemplate.postForEntity("/stadiums", expectedStadium, Stadium.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("Update a stadium")
        public void updateStadium() throws W2PEntityExistsException {
            // given
            Stadium expectedStadium = insertStadium(new Stadium.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Old address")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(new Field("Palau Baulgrama", "Basketball")))
                    .build());

            // when
            expectedStadium.setAddress("new Address");
            ResponseEntity<Stadium> actualStadium = restTemplate.exchange("/stadiums", HttpMethod.PUT, new HttpEntity<>(expectedStadium), Stadium.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualStadium.getBody()).extracting(Stadium::getAddress).isEqualTo("new Address");
        }

        @Test
        @DisplayName("Error updating a stadium")
        public void updateNonCompleteStadium() throws W2PEntityExistsException {
            // given
            Stadium expectedStadium = insertStadium(new Stadium.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("An address")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(new Field("Palau Baulgrama", "Basketball")))
                    .build());

            // when
            expectedStadium.setName(null);
            ResponseEntity<Stadium> actualStadium = restTemplate.exchange("/stadiums", HttpMethod.PUT, new HttpEntity<>(expectedStadium), Stadium.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Delete a stadium")
        public void deleteStadium() throws W2PEntityExistsException {
            // given
            insertStadium(new Stadium.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(new Field("Palau Baulgrama", "Basketball")))
                    .build());

            // when
            ResponseEntity<Stadium> actualStadium = restTemplate.exchange("/stadiums/BFC", HttpMethod.DELETE, new HttpEntity<>(null), Stadium.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Error deleting a stadium")
        public void deleteNonExistingStadium() {
            // when
            ResponseEntity<Stadium> actualStadium = restTemplate.exchange("/stadiums/InvalidStadiumID", HttpMethod.DELETE, new HttpEntity<>(null), Stadium.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

    }

    @Nested
    class GetStadiums {

        @Test
        @DisplayName("Get all stadiums")
        public void getAllStadiums() throws W2PEntityExistsException {
            // given
            insertStadium(new Stadium.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(new Field("Palau Baulgrama", "Basketball")))
                    .build());
            insertStadium(new Stadium.Builder().withId("RMD")
                    .withName("Real Madrid")
                    .withCity("Madrid")
                    .withFields(Arrays.asList(new Field("Santiago Bernabeu", "Soccer")))
                    .build());

            // when
            ResponseEntity<Stadium[]> stadiums = restTemplate.getForEntity("/stadiums", Stadium[].class);

            // then
            assertThat(stadiums).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(stadiums.getBody()).hasSize(2);
            List<String> stadiumsIds = Arrays.stream(stadiums.getBody()).map(Stadium::getId).collect(Collectors.toList());
            assertThat(stadiumsIds).containsAll(Arrays.asList("BFC", "RMD"));
        }

        @Test
        @DisplayName("Get a stadium by ID")
        public void getStadiumById() throws W2PEntityExistsException {
            // given
            insertStadium(new Stadium.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withCity("Barcelona")
                    .build());
            insertStadium(new Stadium.Builder().withId("RMD")
                    .withName("Real Madrid")
                    .withCity("Madrid")
                    .build());

            // when
            ResponseEntity<Stadium> actualStadium = restTemplate.getForEntity("/stadiums/RMD", Stadium.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualStadium.getBody()).extracting(Stadium::getId).isEqualTo("RMD");
        }

        @Test
        @DisplayName("Error getting a stadium by ID")
        public void getNonExistingStadiumById() {
            // when
            ResponseEntity<Stadium> actualStadium = restTemplate.getForEntity("/stadiums/invalidId", Stadium.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Get stadiums by city")
        public void getStadiumsByCity() throws W2PEntityExistsException {
            // given
            insertStadium(new Stadium.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .withCity("Barcelona")
                    .build());
            insertStadium(new Stadium.Builder().withId("RMD")
                    .withName("Real Madrid")
                    .withAddress("Some address")
                    .withCity("Madrid")
                    .build());
            insertStadium(new Stadium.Builder().withId("AMD")
                    .withName("Atletico Madrid")
                    .withAddress("Some address")
                    .withCity("Madrid")
                    .build());

            // when
            ResponseEntity<Stadium[]> actualStadiums = restTemplate.getForEntity("/stadiums/?city=Madrid", Stadium[].class);

            // then
            assertThat(actualStadiums).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            List<String> stadiumsNames = Arrays.stream(actualStadiums.getBody()).map(Stadium::getName).collect(Collectors.toList());
            assertThat(stadiumsNames).containsAll(Arrays.asList("Real Madrid", "Atletico Madrid"));
        }

    }

}
