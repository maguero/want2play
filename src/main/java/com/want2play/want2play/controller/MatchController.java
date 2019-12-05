package com.want2play.want2play.controller;

import com.want2play.want2play.model.Match;
import com.want2play.want2play.service.MatchService;
import org.springframework.web.bind.annotation.*;

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
    public List<Match> getAll() {
        return service.getAllMatches();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Match saveMatch(@RequestBody @Valid Match match, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return service.saveMatch(match);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Match updateMatch(@RequestBody Match match) {
        return service.updateMatch(match);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteMatch(@PathVariable("id") String id, HttpServletResponse response) {
        service.deleteMatch(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Match getMatchById(@PathVariable("id") String id) {
        return service.getById(id);
    }

    @RequestMapping(value = "/", params = "adminPlayer", method = RequestMethod.GET)
    public List<Match> getMatchesByAdminPlayer(@RequestParam("adminPlayer") String adminPlayerId) {
        return service.getMatchesByAdminPlayer(adminPlayerId);
    }

    @RequestMapping(value = "/", params = "state", method = RequestMethod.GET)
    public List<Match> getMatchesByState(@RequestParam("state") String state) {
        return service.getMatchesByState(state);
    }

    @RequestMapping(value = "/", params = {"city", "sport"}, method = RequestMethod.GET)
    public List<Match> getOpenMatchesByCityAndSport(@RequestParam("city") String city, @RequestParam("sport") String sport) {
        return service.getOpenMatchesByCityAndSport(city, sport);
    }

}
