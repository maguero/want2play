package com.want2play.want2play.controller;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.model.Field;
import com.want2play.want2play.model.Stadium;
import com.want2play.want2play.service.StadiumService;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
        try {
            return service.getStadiumById(id);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/", params = "name", method = RequestMethod.GET)
    public List<Stadium> getAll(@RequestParam("city") String city) {
        return service.getStadiumsByCity(city);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Stadium saveStadium(@RequestBody @Valid Stadium stadium, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        try {
            return service.insertStadium(stadium);
        } catch (W2PEntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Stadium updateStadium(@RequestBody @Valid Stadium stadium) {
        try {
            return service.updateStadium(stadium);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteStadium(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            service.deleteStadium(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{stadiumId}", method = RequestMethod.POST)
    public Field saveStadiumField(@PathVariable("stadiumId") String stadiumId, @RequestBody @Valid Field field) {
        throw new NotImplementedException();
    }

}
