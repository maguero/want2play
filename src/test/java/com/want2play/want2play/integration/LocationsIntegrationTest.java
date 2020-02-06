package com.want2play.want2play.integration;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.model.City;
import com.want2play.want2play.model.Country;
import com.want2play.want2play.model.State;
import com.want2play.want2play.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationsIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private LocationService adminService;

    private Country insertCountry(Country country) throws W2PEntityExistsException {
        return adminService.saveCountry(country);
    }

    @BeforeEach
    public void cleanCountries() {
        adminService.getAllCountries().stream().forEach(c -> {
            try {
                adminService.deleteCountry(c.getCode());
            } catch (W2PEntityNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Nested
    class CountryActions {

        @Test
        @DisplayName("Add a Country")
        public void saveCountry() {
            // given
            Country expectedCountry = new Country.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new State("Barcelona")
                            , Arrays.asList(new City("Barcelona"), new City("Badalona"))).build();

            // when
            ResponseEntity<Country> savedCountryResponse = restTemplate.postForEntity("/locations", expectedCountry, Country.class);
            assertThat(savedCountryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            // then
            Country actualCountry = savedCountryResponse.getBody();
            assertThat(actualCountry).isNotNull();
            assertThat(actualCountry).extracting(Country::getCode).isEqualTo("SPN");
            assertThat(actualCountry).extracting(Country::getName).isEqualTo("Spain");
            assertThat(actualCountry.getStates().get(0)).extracting(State::getName).isEqualTo("Barcelona");
        }

        @Test
        @DisplayName("Add a duplicated Country")
        public void saveDuplicatedCountry() throws W2PEntityExistsException {
            // given
            Country expectedCountry = new Country.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new State("Barcelona")
                            , Arrays.asList(new City("Barcelona"), new City("Badalona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<Country> savedCountryResponse = restTemplate.postForEntity("/locations", expectedCountry, Country.class);

            // then
            assertThat(savedCountryResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("Return a Country by ID")
        public void returnCountryById() throws W2PEntityExistsException {
            // given
            Country expectedCountry = new Country.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new State("Barcelona")
                            , Arrays.asList(new City("Barcelona"), new City("Badalona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<Country> savedCountryResponse = restTemplate.getForEntity("/locations/SPN", Country.class);
            assertThat(savedCountryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

            // then
            Country actualCountry = savedCountryResponse.getBody();
            assertThat(actualCountry).isNotNull();
            assertThat(actualCountry).extracting(Country::getCode).isEqualTo("SPN");
        }

        @Test
        @DisplayName("Return a non existing Country")
        public void returnNonExistingCountry() {
            // when
            ResponseEntity<Country> savedCountryResponse = restTemplate.getForEntity("/locations/NonExistingID", Country.class);

            // then
            assertThat(savedCountryResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Return all Countries")
        public void returnAllCountries() throws W2PEntityExistsException {
            // given
            insertCountry(new Country.Builder()
                    .withCode("ARG").withName("Argentina")
                    .withState(new State("Entre Rios")
                            , Arrays.asList(new City("Parana"))).build());
            insertCountry(new Country.Builder()
                    .withCode("URU").withName("Uruguay")
                    .withState(new State("Montevideo")
                            , Arrays.asList(new City("Montevideo"))).build());

            // when
            ResponseEntity<Country[]> savedCountryResponse = restTemplate.getForEntity("/locations", Country[].class);

            // then
            assertThat(savedCountryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            Country[] actualCountries = savedCountryResponse.getBody();
            assertThat(actualCountries).hasSize(2);

            // and
            assertThat(Arrays.stream(actualCountries).map(Country::getCode).collect(Collectors.toList()))
                    .containsAll(Arrays.asList("ARG", "URU"));
        }
    }

    @Nested
    class StateActions {

        @Test
        @DisplayName("Add a State")
        public void addNewState() throws W2PEntityExistsException {
            // given
            Country expectedCountry = new Country.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new State("Barcelona")
                            , Arrays.asList(new City("Barcelona"), new City("Badalona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<Country> actualCountryResponse = restTemplate.postForEntity("/locations/SPN/states", new State("Madrid"), Country.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            Country actualCountry = actualCountryResponse.getBody();
            assertThat(actualCountry).isNotNull();
            assertThat(actualCountry.getStates()).hasSize(2);
            assertThat(actualCountry.getStates().stream().map(State::getName).collect(Collectors.toList()))
                    .containsAll(Arrays.asList("Barcelona", "Madrid"));
        }

        @Test
        @DisplayName("Add a State to an invalid Country")
        public void addNewStateToInvalidCountry() {
            // when
            ResponseEntity<Country> actualCountryResponse = restTemplate.postForEntity("/locations/NNN/states", new State("Madrid"), Country.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Add a duplicated State")
        public void addDuplicatedState() throws W2PEntityExistsException {
            // given
            Country expectedCountry = new Country.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new State("Barcelona")
                            , Arrays.asList(new City("Barcelona"), new City("Badalona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<Country> actualCountryResponse = restTemplate.postForEntity("/locations/SPN/states", new State("Barcelona"), Country.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }

    @Nested
    class CityActions {

        @Test
        @DisplayName("Add a City")
        public void addNewCity() throws W2PEntityExistsException {
            // given
            Country expectedCountry = new Country.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new State("Barcelona")
                            , Arrays.asList(new City("Barcelona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<Country> actualCountryResponse =
                    restTemplate.postForEntity("/locations/SPN/Barcelona/cities",
                            new City("Badalona"),
                            Country.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            Country actualCountry = actualCountryResponse.getBody();
            assertThat(actualCountry).isNotNull();
            List<String> cities = actualCountry.getStates().stream().flatMap(state -> state.getCities().stream()).map(City::getName).collect(Collectors.toList());
            assertThat(cities).containsAll(Arrays.asList("Barcelona", "Badalona"));
        }

        @Test
        @DisplayName("Add a city to an invalid State")
        public void addNewCityToInvalidState() throws W2PEntityExistsException {
            // given
            Country expectedCountry = new Country.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new State("Barcelona")
                            , Arrays.asList(new City("Barcelona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<Country> actualCountryResponse = restTemplate.postForEntity("/locations/SPN/Madrid/cities", new City("Madrid"), Country.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Add a duplicated City")
        public void addDuplicatedCity() throws W2PEntityExistsException {
            // given
            Country expectedCountry = new Country.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new State("Barcelona")
                            , Arrays.asList(new City("Barcelona"), new City("Badalona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<Country> actualCountryResponse = restTemplate.postForEntity("/locations/SPN/Barcelona/cities", new State("Barcelona"), Country.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }
}
