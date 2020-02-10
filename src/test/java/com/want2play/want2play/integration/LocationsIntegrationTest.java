package com.want2play.want2play.integration;

import com.want2play.want2play.dto.CityDto;
import com.want2play.want2play.dto.CountryDto;
import com.want2play.want2play.dto.StateDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
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

    private CountryDto insertCountry(CountryDto country) throws W2PEntityExistsException {
        return adminService.insertCountry(country);
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
            CountryDto expectedCountry = new CountryDto.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new StateDto("Barcelona")
                            , Arrays.asList(new CityDto("Barcelona"), new CityDto("Badalona"))).build();

            // when
            ResponseEntity<CountryDto> savedCountryResponse = restTemplate.postForEntity("/locations", expectedCountry, CountryDto.class);
            assertThat(savedCountryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            // then
            CountryDto actualCountry = savedCountryResponse.getBody();
            assertThat(actualCountry).isNotNull();
            assertThat(actualCountry).extracting(CountryDto::getCode).isEqualTo("SPN");
            assertThat(actualCountry).extracting(CountryDto::getName).isEqualTo("Spain");
            assertThat(actualCountry.getStates().get(0)).extracting(StateDto::getName).isEqualTo("Barcelona");
        }

        @Test
        @DisplayName("Add a duplicated Country")
        public void saveDuplicatedCountry() throws W2PEntityExistsException {
            // given
            CountryDto expectedCountry = new CountryDto.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new StateDto("Barcelona")
                            , Arrays.asList(new CityDto("Barcelona"), new CityDto("Badalona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<CountryDto> savedCountryResponse = restTemplate.postForEntity("/locations", expectedCountry, CountryDto.class);

            // then
            assertThat(savedCountryResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("Return a CountryDto by ID")
        public void returnCountryById() throws W2PEntityExistsException {
            // given
            CountryDto expectedCountry = new CountryDto.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new StateDto("Barcelona")
                            , Arrays.asList(new CityDto("Barcelona"), new CityDto("Badalona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<CountryDto> savedCountryResponse = restTemplate.getForEntity("/locations/SPN", CountryDto.class);
            assertThat(savedCountryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

            // then
            CountryDto actualCountry = savedCountryResponse.getBody();
            assertThat(actualCountry).isNotNull();
            assertThat(actualCountry).extracting(CountryDto::getCode).isEqualTo("SPN");
        }

        @Test
        @DisplayName("Return a non existing Country")
        public void returnNonExistingCountry() {
            // when
            ResponseEntity<CountryDto> savedCountryResponse = restTemplate.getForEntity("/locations/NonExistingID", CountryDto.class);

            // then
            assertThat(savedCountryResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Return all Countries")
        public void returnAllCountries() throws W2PEntityExistsException {
            // given
            insertCountry(new CountryDto.Builder()
                    .withCode("ARG").withName("Argentina")
                    .withState(new StateDto("Entre Rios")
                            , Arrays.asList(new CityDto("Parana"))).build());
            insertCountry(new CountryDto.Builder()
                    .withCode("URU").withName("Uruguay")
                    .withState(new StateDto("Montevideo")
                            , Arrays.asList(new CityDto("Montevideo"))).build());

            // when
            ResponseEntity<CountryDto[]> savedCountryResponse = restTemplate.getForEntity("/locations", CountryDto[].class);

            // then
            assertThat(savedCountryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            CountryDto[] actualCountries = savedCountryResponse.getBody();
            assertThat(actualCountries).hasSize(2);

            // and
            assertThat(Arrays.stream(actualCountries).map(CountryDto::getCode).collect(Collectors.toList()))
                    .containsAll(Arrays.asList("ARG", "URU"));
        }
    }

    @Nested
    class StateActions {

        @Test
        @DisplayName("Add a State")
        public void addNewState() throws W2PEntityExistsException {
            // given
            CountryDto expectedCountry = new CountryDto.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new StateDto("Barcelona")
                            , Arrays.asList(new CityDto("Barcelona"), new CityDto("Badalona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<CountryDto> actualCountryResponse = restTemplate.postForEntity("/locations/SPN/states", new StateDto("Madrid"), CountryDto.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            CountryDto actualCountry = actualCountryResponse.getBody();
            assertThat(actualCountry).isNotNull();
            assertThat(actualCountry.getStates()).hasSize(2);
            assertThat(actualCountry.getStates().stream().map(StateDto::getName).collect(Collectors.toList()))
                    .containsAll(Arrays.asList("Barcelona", "Madrid"));
        }

        @Test
        @DisplayName("Add a State to an invalid Country")
        public void addNewStateToInvalidCountry() {
            // when
            ResponseEntity<CountryDto> actualCountryResponse = restTemplate.postForEntity("/locations/NNN/states", new StateDto("Madrid"), CountryDto.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Add a duplicated State")
        public void addDuplicatedState() throws W2PEntityExistsException {
            // given
            CountryDto expectedCountry = new CountryDto.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new StateDto("Barcelona")
                            , Arrays.asList(new CityDto("Barcelona"), new CityDto("Badalona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<CountryDto> actualCountryResponse = restTemplate.postForEntity("/locations/SPN/states", new StateDto("Barcelona"), CountryDto.class);

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
            CountryDto expectedCountry = new CountryDto.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new StateDto("Barcelona")
                            , Arrays.asList(new CityDto("Barcelona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<CountryDto> actualCountryResponse =
                    restTemplate.postForEntity("/locations/SPN/Barcelona/cities",
                            new CityDto("Badalona"),
                            CountryDto.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            CountryDto actualCountry = actualCountryResponse.getBody();
            assertThat(actualCountry).isNotNull();
            List<String> cities = actualCountry.getStates().stream().flatMap(state -> state.getCities().stream()).map(CityDto::getName).collect(Collectors.toList());
            assertThat(cities).containsAll(Arrays.asList("Barcelona", "Badalona"));
        }

        @Test
        @DisplayName("Add a city to an invalid State")
        public void addNewCityToInvalidState() throws W2PEntityExistsException {
            // given
            CountryDto expectedCountry = new CountryDto.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new StateDto("Barcelona")
                            , Arrays.asList(new CityDto("Barcelona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<CountryDto> actualCountryResponse = restTemplate.postForEntity("/locations/SPN/Madrid/cities", new CityDto("Madrid"), CountryDto.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Add a duplicated City")
        public void addDuplicatedCity() throws W2PEntityExistsException {
            // given
            CountryDto expectedCountry = new CountryDto.Builder()
                    .withCode("SPN").withName("Spain")
                    .withState(new StateDto("Barcelona")
                            , Arrays.asList(new CityDto("Barcelona"), new CityDto("Badalona"))).build();
            insertCountry(expectedCountry);

            // when
            ResponseEntity<CountryDto> actualCountryResponse = restTemplate.postForEntity("/locations/SPN/Barcelona/cities", new StateDto("Barcelona"), CountryDto.class);

            // then
            assertThat(actualCountryResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }
}
