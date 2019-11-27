package com.want2play.want2play.controller;

import com.want2play.want2play.model.Field;
import com.want2play.want2play.model.Stadium;
import com.want2play.want2play.service.AdministrationService;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/stadiums")
public class StadiumController {

    @Autowired
    AdministrationService service;

    @RequestMapping(value = "/{city}", method = RequestMethod.GET)
    public List<Stadium> getAll(@PathVariable("city") String city) {
        return service.getStadiumsByCity(city);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Stadium saveStadium(@RequestBody Stadium stadium) {
        return service.saveStadium(stadium);
    }

    @RequestMapping(value = "/{stadiumId}", method = RequestMethod.POST)
    public Field saveStadiumField(@PathVariable("stadiumId") String stadiumId, @RequestBody Field field) {
        throw new NotImplementedException();
    }

}
