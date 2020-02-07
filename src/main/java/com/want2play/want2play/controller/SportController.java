package com.want2play.want2play.controller;

import com.want2play.want2play.dto.SportDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.service.SportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        try {
            return service.getSportById(id);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public SportDto saveSport(@RequestBody @Valid SportDto sport, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        try {
            return service.insertSport(sport);
        } catch (W2PEntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public SportDto updateSport(@PathVariable("id") String id,
                                @RequestBody @Valid SportDto.SportUpdateDto sport) {
        try {
            return service.updateSport(id, sport);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteSport(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            service.deleteSport(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
