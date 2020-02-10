package com.want2play.want2play.service;

import com.want2play.want2play.dto.CityDto;
import com.want2play.want2play.dto.CountryDto;
import com.want2play.want2play.dto.StateDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;

import java.util.List;

public interface LocationService {

    List<CountryDto> getAllCountries();

    CountryDto getCountryByCode(String code) throws W2PEntityNotFoundException;

    CountryDto insertCountry(CountryDto country) throws W2PEntityExistsException;

    CountryDto addState(String countryCode, StateDto state) throws W2PEntityNotFoundException, W2PEntityExistsException;

    CountryDto addCity(String countryCode, String state, CityDto city) throws W2PEntityExistsException, W2PEntityNotFoundException;

    void deleteCountry(String countryCode) throws W2PEntityNotFoundException;

}