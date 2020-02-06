package com.want2play.want2play.service;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.model.City;
import com.want2play.want2play.model.Country;
import com.want2play.want2play.model.State;
import com.want2play.want2play.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private CountryRepository countryRepository;

    public LocationService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Country getCountryByCode(String code) throws W2PEntityNotFoundException {
        return countryRepository.findById(code)
                .orElseThrow(() -> new W2PEntityNotFoundException(String.format("Country #%s not found.", code)));
    }

    public Country saveCountry(Country country) throws W2PEntityExistsException {
        if (countryRepository.existsById(country.getCode())) {
            throw new W2PEntityExistsException(String.format("Country #%s already exists.", country.getCode()));
        }
        return countryRepository.save(country);
    }

    public Country addState(String countryCode, State state) throws W2PEntityNotFoundException, W2PEntityExistsException {
        Country country = countryRepository.findById(countryCode)
                .orElseThrow(() -> new W2PEntityNotFoundException(String.format("Country #%s not found.", countryCode)));
        if (country.getStates().stream().anyMatch(s -> s.getName().equalsIgnoreCase(state.getName()))) {
            throw new W2PEntityExistsException(String.format("State #%s not found.", country.getCode()));
        } else {
            country.getStates().add(state);
            return countryRepository.save(country);
        }
    }

    public Country addCity(String countryCode, String state, City city) throws W2PEntityExistsException, W2PEntityNotFoundException {
        Country country = countryRepository.findById(countryCode)
                .orElseThrow(() -> new W2PEntityNotFoundException(String.format("Country #%s not found.", countryCode)));
        if (country.getStates().stream().noneMatch(s -> s.getName().equalsIgnoreCase(state))) {
            throw new W2PEntityNotFoundException(String.format("State #%s not found.", state));
        } else if (country.getStates().stream()
                .flatMap(s -> s.getCities().stream())
                .anyMatch(c -> c.getName().equalsIgnoreCase(city.getName()))) {
            throw new W2PEntityExistsException(String.format("City #%s already exists.", city.getName()));
        } else {
            List<State> states = country.getStates();
            State matchState = states.stream().filter(s -> s.getName().equalsIgnoreCase(state)).findFirst().get();
            states.get(states.indexOf(matchState)).getCities().add(city);
            return countryRepository.save(country);
        }
    }

    public void deleteCountry(String countryCode) throws W2PEntityNotFoundException {
        if (!countryRepository.existsById(countryCode)) {
            throw new W2PEntityNotFoundException(String.format("Country #%s not found.", countryCode));
        }
        countryRepository.deleteById(countryCode);
    }

}