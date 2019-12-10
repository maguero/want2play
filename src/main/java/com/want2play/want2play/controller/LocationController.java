package com.want2play.want2play.controller;

import com.want2play.want2play.exception.W2PNotFoundException;
import com.want2play.want2play.model.City;
import com.want2play.want2play.model.Country;
import com.want2play.want2play.model.State;
import com.want2play.want2play.service.LocationService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/locations")
public class LocationController {

    private LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Country> getAllCountries() {
        return service.getAllCountries();
    }

    @RequestMapping(value = "/{countryCode}", method = RequestMethod.GET)
    public Country getCountryByCode(@PathVariable("countryCode") String countryCode) {
        Optional<Country> countryByCode = service.getCountryByCode(countryCode);
        if (countryByCode.isEmpty()) {
            throw new W2PNotFoundException();
        }
        return countryByCode.get();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Country saveCountry(@RequestBody Country country, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return service.saveCountry(country);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Country updateCountry(@RequestBody @Valid Country country) {
        return service.saveCountry(country);
    }

    @RequestMapping(value = "/{countryCode}", method = RequestMethod.DELETE)
    public void deleteCountry(@PathVariable("countryCode") String countryCode) {
        service.deleteCountry(countryCode);
    }

    @RequestMapping(value = "/{countryCode}/states", method = RequestMethod.POST)
    public Country addState(@PathVariable("countryCode") String countryCode, @RequestBody @Valid State state) {
        return service.addState(countryCode, state);
    }

    @RequestMapping(value = "/{countryCode}/{state}/cities", method = RequestMethod.POST)
    public Country addCity(@RequestBody @Valid City city,
                           @PathVariable("countryCode") String countryCode,
                           @PathVariable("state") String state) {
        return service.addCity(countryCode, state, city);
    }

}
