package com.want2play.want2play.controller;

import com.want2play.want2play.model.Country;
import com.want2play.want2play.service.AdministrationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/locations")
public class LocationController {

    private AdministrationService service;

    public LocationController(AdministrationService service) {
        this.service = service;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Country> getAll() {
        return service.getAllCountries();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Country saveLocation(@RequestBody Country country) {
        return service.saveCountry(country);
    }

}
