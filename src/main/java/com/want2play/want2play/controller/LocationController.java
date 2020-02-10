package com.want2play.want2play.controller;

import com.want2play.want2play.dto.CityDto;
import com.want2play.want2play.dto.CountryDto;
import com.want2play.want2play.dto.StateDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/locations")
public class LocationController {

    private LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CountryDto> getAllCountries() {
        return service.getAllCountries();
    }

    @RequestMapping(value = "/{countryCode}", method = RequestMethod.GET)
    public CountryDto getCountryByCode(@PathVariable("countryCode") String countryCode) {
        try {
            return service.getCountryByCode(countryCode);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public CountryDto saveCountry(@RequestBody CountryDto country, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_CREATED);
            return service.insertCountry(country);
        } catch (W2PEntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public CountryDto updateCountry(@RequestBody @Valid CountryDto country) {
        try {
            // TODO review
            return service.insertCountry(country);
        } catch (W2PEntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{countryCode}", method = RequestMethod.DELETE)
    public void deleteCountry(@PathVariable("countryCode") String countryCode) {
        try {
            service.deleteCountry(countryCode);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{countryCode}/states", method = RequestMethod.POST)
    public CountryDto addState(@PathVariable("countryCode") String countryCode, @RequestBody @Valid StateDto state) {
        try {
            return service.addState(countryCode, state);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (W2PEntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{countryCode}/{state}/cities", method = RequestMethod.POST)
    public CountryDto addCity(@RequestBody @Valid CityDto city,
                              @PathVariable("countryCode") String countryCode,
                              @PathVariable("state") String state) {
        try {
            return service.addCity(countryCode, state, city);
        } catch (W2PEntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
