package com.want2play.want2play.controller;

import com.want2play.want2play.dto.MatchDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.service.MatchService;
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
@RequestMapping(value = "/matches")
public class MatchController {

    private MatchService service;

    public MatchController(MatchService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MatchDto> getAll() {
        return service.getAllMatches();
    }

    @RequestMapping(method = RequestMethod.POST)
    public MatchDto insertMatch(@RequestBody @Valid MatchDto match, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        try {
            return service.insertMatch(match);
        } catch (W2PEntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public MatchDto updateMatch(@RequestBody MatchDto match) {
        return service.updateMatch(match);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteMatch(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            service.deleteMatch(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MatchDto getMatchById(@PathVariable("id") String id) {
        try {
            return service.getById(id);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/", params = "adminPlayer", method = RequestMethod.GET)
    public List<MatchDto> getMatchesByAdminPlayer(@RequestParam("adminPlayer") String adminPlayerId) {
        return service.getMatchesByAdminPlayer(adminPlayerId);
    }

    @RequestMapping(value = "/", params = "state", method = RequestMethod.GET)
    public List<MatchDto> getMatchesByState(@RequestParam("state") String state) {
        return service.getMatchesByState(state);
    }

    @RequestMapping(value = "/", params = {"city", "sport"}, method = RequestMethod.GET)
    public List<MatchDto> getOpenMatchesByCityAndSport(@RequestParam("city") String city, @RequestParam("sport") String sport) {
        return service.getOpenMatchesByCityAndSport(city, sport);
    }

}
