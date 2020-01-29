package com.want2play.want2play.controller;

import com.want2play.want2play.dto.SportDto;
import com.want2play.want2play.service.SportService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/sports")
public class SportController {

    private SportService service;

    public SportController(SportService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<SportDto> getAll() {
        return service.getAllSports();
    }

    @RequestMapping(value = "/", params = "name", method = RequestMethod.GET)
    public List<SportDto> getSportsByName(@RequestParam("name") String name) {
        return service.getSportsByName(name);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SportDto getById(@PathVariable("id") String id) {
        return service.getSportById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public SportDto saveSport(@RequestBody @Valid SportDto sport, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return service.insertSport(sport);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public SportDto updateSport(@RequestBody @Valid SportDto sport) {
        return service.updateSport(sport);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteSport(@PathVariable("id") String id, HttpServletResponse response) {
        service.deleteSport(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
