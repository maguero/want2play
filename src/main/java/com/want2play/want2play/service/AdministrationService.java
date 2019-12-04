package com.want2play.want2play.service;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PNotFoundException;
import com.want2play.want2play.model.*;
import com.want2play.want2play.repository.CountryRepository;
import com.want2play.want2play.repository.PlayerRepository;
import com.want2play.want2play.repository.SportRepository;
import com.want2play.want2play.repository.StadiumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministrationService {

    private SportRepository sportRepository;
    private CountryRepository countryRepository;
    private StadiumRepository stadiumRepository;
    private PlayerRepository playerRepository;

    public AdministrationService(SportRepository sportRepository, CountryRepository countryRepository, StadiumRepository stadiumRepository, PlayerRepository playerRepository) {
        this.sportRepository = sportRepository;
        this.countryRepository = countryRepository;
        this.stadiumRepository = stadiumRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * --- SPORT methods ---
     */

    public Sport saveSport(Sport sport) {
        return sportRepository.save(sport);
    }

    public List<Sport> getAllSports() {
        return sportRepository.findAll();
    }

    /**
     * --- COUNTRY methods ---
     */

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Optional<Country> getCountryByCode(String code) {
        return countryRepository.findById(code);
    }

    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    public Country addState(String countryCode, State state) {
        Optional<Country> country = countryRepository.findById(countryCode);
        if (country.isEmpty()) {
            throw new W2PNotFoundException();
        } else if (country.get().getStates().stream().anyMatch(s -> s.getName().equalsIgnoreCase(state.getName()))) {
            throw new W2PEntityExistsException();
        } else {
            country.get().getStates().add(state);
            return countryRepository.save(country.get());
        }
    }

    public Country addCity(String countryCode, String state, City city) {
        Optional<Country> country = countryRepository.findById(countryCode);
        if (country.isEmpty()
                || country.get().getStates().stream().noneMatch(s -> s.getName().equalsIgnoreCase(state))) {
            throw new W2PNotFoundException();
        } else if (country.get().getStates().stream()
                .flatMap(s -> s.getCities().stream())
                .anyMatch(c -> c.getName().equalsIgnoreCase(city.getName()))) {
            throw new W2PEntityExistsException();
        } else {
            List<State> states = country.get().getStates();
            State matchState = states.stream().filter(s -> s.getName().equalsIgnoreCase(state)).findFirst().get();
            states.get(states.indexOf(matchState)).getCities().add(city);
            return countryRepository.save(country.get());
        }
    }

    public void deleteCountry(String countryCode) {
        Optional<Country> country = countryRepository.findById(countryCode);
        if (country.isPresent()) {
            countryRepository.delete(country.get());
        } else {
            throw new W2PNotFoundException();
        }
    }

    /**
     * --- STADIUM methods ---
     */

    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }

    public List<Stadium> getStadiumsByCity(String city) {
        return stadiumRepository.findByCity(city);
    }

    public Stadium saveStadium(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }

    /**
     * --- PLAYER methods ---
     */

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

}
