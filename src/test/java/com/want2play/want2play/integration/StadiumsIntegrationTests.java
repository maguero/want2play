package com.want2play.want2play.integration;

import com.want2play.want2play.dto.FieldDto;
import com.want2play.want2play.dto.SportDto;
import com.want2play.want2play.dto.StadiumDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
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

    private StadiumDto insertStadium(StadiumDto stadium) throws W2PEntityExistsException {
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
            List<FieldDto> fields = Arrays.asList(
                    new FieldDto("Camp Nou", new SportDto("SCC", "Soccer", 11)),
                    new FieldDto("Palau Baulgrama", new SportDto("BSK", "Basketball", 5)));
            StadiumDto expectedStadium = new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .withCity("Barcelona")
                    .withFields(fields)
                    .build();

            // when
            ResponseEntity<StadiumDto> actualStadium = restTemplate.postForEntity("/stadiums", expectedStadium, StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CREATED);
            assertThat(actualStadium.getBody()).isEqualTo(expectedStadium);
        }

        @Test
        @DisplayName("Error inserting a non complete stadium")
        public void insertNonCompleteStadium() {
            // given
            StadiumDto expectedStadium = new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .build();

            // when
            ResponseEntity<StadiumDto> actualStadium = restTemplate.postForEntity("/stadiums", expectedStadium, StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Error inserting a duplicate stadium")
        public void insertDuplicateStadium() throws W2PEntityExistsException {
            // given
            StadiumDto expectedStadium = new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(new FieldDto("Palau Baulgrama", new SportDto("BSK", "Basketball", 5))))
                    .build();
            insertStadium(expectedStadium);

            // when
            ResponseEntity<StadiumDto> actualStadium = restTemplate.postForEntity("/stadiums", expectedStadium, StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("Update a stadium")
        public void updateStadium() throws W2PEntityExistsException {
            // given
            StadiumDto expectedStadium = insertStadium(new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Old address")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(new FieldDto("Palau Baulgrama", new SportDto("BSK", "Basketball", 5))))
                    .build());

            // when
            expectedStadium.setAddress("new Address");
            ResponseEntity<StadiumDto> actualStadium = restTemplate.exchange("/stadiums/BFC", HttpMethod.PUT, new HttpEntity<>(expectedStadium), StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualStadium.getBody()).extracting(StadiumDto::getAddress).isEqualTo("new Address");
        }

        @Test
        @DisplayName("Error updating a stadium")
        public void updateNonCompleteStadium() throws W2PEntityExistsException {
            // given
            StadiumDto expectedStadium = insertStadium(new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("An address")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(new FieldDto("Palau Baulgrama", new SportDto("BSK", "Basketball", 5))))
                    .build());

            // when
            expectedStadium.setName(null);
            ResponseEntity<StadiumDto> actualStadium = restTemplate.exchange("/stadiums/BFC", HttpMethod.PUT, new HttpEntity<>(expectedStadium), StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Delete a stadium")
        public void deleteStadium() throws W2PEntityExistsException {
            // given
            insertStadium(new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(new FieldDto("Palau Baulgrama", new SportDto("BSK", "Basketball", 5))))
                    .build());

            // when
            ResponseEntity<StadiumDto> actualStadium = restTemplate.exchange("/stadiums/BFC", HttpMethod.DELETE, new HttpEntity<>(null), StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Error deleting a stadium")
        public void deleteNonExistingStadium() {
            // when
            ResponseEntity<StadiumDto> actualStadium = restTemplate.exchange("/stadiums/InvalidStadiumID", HttpMethod.DELETE, new HttpEntity<>(null), StadiumDto.class);

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
            insertStadium(new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(new FieldDto("Palau Baulgrama", new SportDto("BSK", "Basketball", 5))))
                    .build());
            insertStadium(new StadiumDto.Builder().withId("RMD")
                    .withName("Real Madrid")
                    .withCity("Madrid")
                    .withFields(Arrays.asList(new FieldDto("Santiago Bernabeu", new SportDto("SCC", "Soccer", 11))))
                    .build());

            // when
            ResponseEntity<StadiumDto[]> stadiums = restTemplate.getForEntity("/stadiums", StadiumDto[].class);

            // then
            assertThat(stadiums).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(stadiums.getBody()).hasSize(2);
            List<String> stadiumsIds = Arrays.stream(stadiums.getBody()).map(StadiumDto::getId).collect(Collectors.toList());
            assertThat(stadiumsIds).containsAll(Arrays.asList("BFC", "RMD"));
        }

        @Test
        @DisplayName("Get a stadium by ID")
        public void getStadiumById() throws W2PEntityExistsException {
            // given
            insertStadium(new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withCity("Barcelona")
                    .build());
            insertStadium(new StadiumDto.Builder().withId("RMD")
                    .withName("Real Madrid")
                    .withCity("Madrid")
                    .build());

            // when
            ResponseEntity<StadiumDto> actualStadium = restTemplate.getForEntity("/stadiums/RMD", StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            assertThat(actualStadium.getBody()).extracting(StadiumDto::getId).isEqualTo("RMD");
        }

        @Test
        @DisplayName("Error getting a stadium by ID")
        public void getNonExistingStadiumById() {
            // when
            ResponseEntity<StadiumDto> actualStadium = restTemplate.getForEntity("/stadiums/invalidId", StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Get stadiums by city")
        public void getStadiumsByCity() throws W2PEntityExistsException {
            // given
            insertStadium(new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .withCity("Barcelona")
                    .build());
            insertStadium(new StadiumDto.Builder().withId("RMD")
                    .withName("Real Madrid")
                    .withAddress("Some address")
                    .withCity("Madrid")
                    .build());
            insertStadium(new StadiumDto.Builder().withId("AMD")
                    .withName("Atletico Madrid")
                    .withAddress("Some address")
                    .withCity("Madrid")
                    .build());

            // when
            ResponseEntity<StadiumDto[]> actualStadiums = restTemplate.getForEntity("/stadiums/?city=Madrid", StadiumDto[].class);

            // then
            assertThat(actualStadiums).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);
            List<String> stadiumsNames = Arrays.stream(actualStadiums.getBody()).map(StadiumDto::getName).collect(Collectors.toList());
            assertThat(stadiumsNames).containsAll(Arrays.asList("Real Madrid", "Atletico Madrid"));
        }

    }

    @Nested
    class AddFields {

        @Test
        @DisplayName("Insert an extra stadium field")
        public void insertAField() throws W2PEntityExistsException {
            // given
            insertStadium(new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(
                            new FieldDto("Camp Nou", new SportDto("SCC", "Soccer", 11))))
                    .build());

            FieldDto expectedField = new FieldDto("Palau Baulgrama", new SportDto("BSK", "Basketball", 5));

            // when
            ResponseEntity<StadiumDto> actualStadium = restTemplate.postForEntity("/stadiums/BFC/fields", expectedField, StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.OK);

            assertThat(actualStadium.getBody().getFields()).containsOnly(
                    new FieldDto("Camp Nou", new SportDto("SCC", "Soccer", 11)),
                    new FieldDto("Palau Baulgrama", new SportDto("BSK", "Basketball", 5))
            );
        }

        @Test
        @DisplayName("Error inserting a field for a non existing Stadium")
        public void insertNonExistingStadiumField() throws W2PEntityExistsException {
            // given

            FieldDto expectedField = new FieldDto("Palau Baulgrama", new SportDto("BSK", "Basketball", 5));

            // when
            ResponseEntity<StadiumDto> actualStadium = restTemplate.postForEntity("/stadiums/NONEXISTING/fields", expectedField, StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Error inserting a duplicate stadium field")
        public void insertADuplicatedField() throws W2PEntityExistsException {
            // given
            insertStadium(new StadiumDto.Builder().withId("BFC")
                    .withName("Barcelona FC")
                    .withAddress("Some address")
                    .withCity("Barcelona")
                    .withFields(Arrays.asList(
                            new FieldDto("Camp Nou", new SportDto("SCC", "Soccer", 11))))
                    .build());

            FieldDto expectedField = new FieldDto("Camp Nou", new SportDto("SCC", "Soccer", 11));

            // when
            ResponseEntity<StadiumDto> actualStadium = restTemplate.postForEntity("/stadiums/BFC/fields", expectedField, StadiumDto.class);

            // then
            assertThat(actualStadium).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.CONFLICT);
        }

    }

}
