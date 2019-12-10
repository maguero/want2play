package com.want2play.want2play.controller;

import com.want2play.want2play.model.Player;
import com.want2play.want2play.service.PlayerService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/players")
public class PlayerController {

    private PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Player> getAll() {
        return service.getAllPlayers();
    }

    @RequestMapping(value = "/", params = "name", method = RequestMethod.GET)
    public List<Player> getPlayersByName(@RequestParam("name") String name) {
        return service.getPlayersByName(name);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Player getById(@PathVariable("id") String id) {
        return service.getPlayerById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Player savePlayer(@RequestBody @Valid Player player, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return service.insertPlayer(player);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Player updatePlayer(@RequestBody @Valid Player player) {
        return service.updatePlayer(player);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePlayer(@PathVariable("id") String id, HttpServletResponse response) {
        service.deletePlayer(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
