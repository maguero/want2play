package com.want2play.want2play.controller;

import com.want2play.want2play.dto.PlayerDto;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public List<PlayerDto> getAll() {
        return service.getAllPlayers();
    }

    @RequestMapping(value = "/", params = "name", method = RequestMethod.GET)
    public List<PlayerDto> getPlayersByName(@RequestParam("name") String name) {
        return service.getPlayersByName(name);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PlayerDto getById(@PathVariable("id") String id) {
        try {
            return service.getPlayerById(id);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public PlayerDto insertPlayer(@RequestBody @Valid PlayerDto player, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return service.insertPlayer(player);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PlayerDto updatePlayer(@PathVariable("id") String playerId,
                                  @RequestBody @Valid PlayerDto.PlayerUpdateDto player) {
        try {
            return service.updatePlayer(playerId, player);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePlayer(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            service.deletePlayer(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (W2PEntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
