package com.want2play.want2play.controller;

import com.want2play.want2play.model.Sport;
import com.want2play.want2play.service.AdministrationService;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/sports")
public class SportController {

    @Autowired
    AdministrationService service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Sport> getAll() {
        return service.getAllSports();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Sport saveSport(@RequestBody Sport sport) {
        return service.saveSport(sport);
    }

}
