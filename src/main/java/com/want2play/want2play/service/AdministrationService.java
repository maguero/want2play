package com.want2play.want2play.service;

import com.want2play.want2play.model.*;
import com.want2play.want2play.repository.CountryRepository;
import com.want2play.want2play.repository.PlayerRepository;
import com.want2play.want2play.repository.SportRepository;
import com.want2play.want2play.repository.StadiumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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
