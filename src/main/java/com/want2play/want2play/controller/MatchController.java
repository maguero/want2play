package com.want2play.want2play.controller;

import com.want2play.want2play.model.Match;
import com.want2play.want2play.service.MatchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/matches")
public class MatchController {

    private MatchService service;

    public MatchController(MatchService service) {
        this.service = service;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Match> getAll() {
        return service.getAllMatches();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Match saveMatch(@RequestBody Match match) {
        return service.saveMatch(match);
    }

}
