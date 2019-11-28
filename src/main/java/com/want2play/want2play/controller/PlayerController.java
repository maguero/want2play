package com.want2play.want2play.controller;

import com.want2play.want2play.model.Player;
import com.want2play.want2play.service.AdministrationService;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/players")
public class PlayerController {

    private AdministrationService service;

    public PlayerController(AdministrationService service) {
        this.service = service;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Player> getAll() {
        return service.getAllPlayers();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Player saveStadium(@RequestBody Player player) {
        throw new NotImplementedException();
    }

}
