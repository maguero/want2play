package com.want2play.want2play.service;

import com.want2play.want2play.dto.PlayerDto;
import com.want2play.want2play.exception.W2PEntityNotFoundException;

import java.util.List;

public interface PlayerService {

    List<PlayerDto> getAllPlayers();

    List<PlayerDto> getPlayersByName(String name);

    PlayerDto getPlayerById(String id) throws W2PEntityNotFoundException;

    PlayerDto insertPlayer(PlayerDto player);

    PlayerDto updatePlayer(String playerId, PlayerDto.PlayerUpdateDto player) throws W2PEntityNotFoundException;

    void deletePlayer(String playerId) throws W2PEntityNotFoundException;

}