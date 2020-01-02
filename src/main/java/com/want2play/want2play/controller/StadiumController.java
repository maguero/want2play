package com.want2play.want2play.controller;

import com.want2play.want2play.model.Field;
import com.want2play.want2play.model.Stadium;
import com.want2play.want2play.service.StadiumService;
import org.apache.commons.lang.NotImplementedException;
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
@RequestMapping(value = "/stadiums")
public class StadiumController {

    private StadiumService service;

    public StadiumController(StadiumService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Stadium> getAll() {
        return service.getAllStadiums();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Stadium getById(@PathVariable("id") String id) {
        return service.getStadiumById(id);
    }

    @RequestMapping(value = "/", params = "name", method = RequestMethod.GET)
    public List<Stadium> getAll(@RequestParam("city") String city) {
        return service.getStadiumsByCity(city);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Stadium saveStadium(@RequestBody @Valid Stadium stadium, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return service.insertStadium(stadium);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Stadium updateStadium(@RequestBody @Valid Stadium stadium) {
        return service.updateStadium(stadium);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteStadium(@PathVariable("id") String id, HttpServletResponse response) {
        service.deleteStadium(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @RequestMapping(value = "/{stadiumId}", method = RequestMethod.POST)
    public Field saveStadiumField(@PathVariable("stadiumId") String stadiumId, @RequestBody @Valid Field field) {
        throw new NotImplementedException();
    }

}
