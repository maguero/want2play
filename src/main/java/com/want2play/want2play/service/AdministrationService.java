package com.want2play.want2play.service;

import com.want2play.want2play.model.*;
import com.want2play.want2play.repository.CountryRepository;
import com.want2play.want2play.repository.PlayerRepository;
import com.want2play.want2play.repository.SportRepository;
import com.want2play.want2play.repository.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AdministrationService {

    @Autowired
    SportRepository sportRepository;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    StadiumRepository stadiumRepository;

    @Autowired
    PlayerRepository playerRepository;

    public Sport saveSport(Sport sport) {
        return sportRepository.save(sport);
    }

    public List<Sport> getAllSports() {
        return sportRepository.findAll();
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    public Country addState(String countryCode, State state) {
        Optional<Country> country = countryRepository.findById(countryCode);
        if (country.isPresent()) {
            country.get().getStates().add(state);
            return countryRepository.save(country.get());
        } else {
            throw new NoSuchElementException();
        }
    }

    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }

    public List<Stadium> getStadiumsByCity(String city) {
        return stadiumRepository.findByCity(city);
    }

    public Stadium saveStadium(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

}
