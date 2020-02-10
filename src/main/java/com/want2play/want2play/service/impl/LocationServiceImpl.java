package com.want2play.want2play.service.impl;

import com.want2play.want2play.dto.CityDto;
import com.want2play.want2play.dto.CountryDto;
import com.want2play.want2play.dto.StateDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.model.City;
import com.want2play.want2play.model.Country;
import com.want2play.want2play.model.State;
import com.want2play.want2play.repository.CountryRepository;
import com.want2play.want2play.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    private CountryRepository countryRepository;
    private ModelMapper mapper;

    public LocationServiceImpl(CountryRepository countryRepository, ModelMapper mapper) {
        this.countryRepository = countryRepository;
        this.mapper = mapper;
    }

    public List<CountryDto> getAllCountries() {
        return convertToDto(countryRepository.findAll());
    }

    public CountryDto getCountryByCode(String code) throws W2PEntityNotFoundException {
        return convertToDto(
                countryRepository.findById(code)
                        .orElseThrow(() -> new W2PEntityNotFoundException(String.format("Country #%s not found.", code)))
        );
    }

    public CountryDto insertCountry(CountryDto country) throws W2PEntityExistsException {
        if (countryRepository.existsById(country.getCode())) {
            throw new W2PEntityExistsException(String.format("Country #%s already exists.", country.getCode()));
        }
        return convertToDto(countryRepository.save(convertToEntity(country)));
    }

    public CountryDto addState(String countryCode, StateDto state) throws W2PEntityNotFoundException, W2PEntityExistsException {
        Country country = countryRepository.findById(countryCode)
                .orElseThrow(() -> new W2PEntityNotFoundException(String.format("Country #%s not found.", countryCode)));
        if (country.getStates().stream().anyMatch(s -> s.getName().equalsIgnoreCase(state.getName()))) {
            throw new W2PEntityExistsException(String.format("State #%s not found.", country.getCode()));
        } else {
            country.getStates().add(mapper.map(state, State.class));
            return convertToDto(countryRepository.save(country));
        }
    }

    public CountryDto addCity(String countryCode, String state, CityDto city) throws W2PEntityExistsException, W2PEntityNotFoundException {
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
            states.get(states.indexOf(matchState)).getCities().add(mapper.map(city, City.class));
            return convertToDto(countryRepository.save(country));
        }
    }

    public void deleteCountry(String countryCode) throws W2PEntityNotFoundException {
        if (!countryRepository.existsById(countryCode)) {
            throw new W2PEntityNotFoundException(String.format("Country #%s not found.", countryCode));
        }
        countryRepository.deleteById(countryCode);
    }

    private CountryDto convertToDto(Country sport) {
        return mapper.map(sport, CountryDto.class);
    }

    private List<CountryDto> convertToDto(List<Country> sports) {
        return sports.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private Country convertToEntity(CountryDto sport) {
        return mapper.map(sport, Country.class);
    }

    private List<Country> convertToEntity(List<CountryDto> sports) {
        return sports.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

}