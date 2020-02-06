package com.want2play.want2play.service.impl;

import com.want2play.want2play.dto.PlayerDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.exception.W2PNotFoundException;
import com.want2play.want2play.model.Player;
import com.want2play.want2play.repository.PlayerRepository;
import com.want2play.want2play.service.PlayerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;
    private ModelMapper mapper;

    public PlayerServiceImpl(PlayerRepository playerRepository, ModelMapper mapper) {
        this.playerRepository = playerRepository;
        this.mapper = mapper;
    }

    public List<PlayerDto> getAllPlayers() {
        return convertToDto(playerRepository.findAll());
    }

    public List<PlayerDto> getPlayersByName(String name) {
        return convertToDto(playerRepository.findByNameContaining(name));
    }

    public PlayerDto getPlayerById(String id) throws W2PEntityNotFoundException {
        return convertToDto(playerRepository.findById(id)
                .orElseThrow(() -> new W2PEntityNotFoundException(String.format("Player #%s not found.", id))));
    }

    public PlayerDto insertPlayer(PlayerDto player) {
        if (playerRepository.existsById(player.getId())) {
            throw new W2PEntityExistsException();
        }
        return convertToDto(playerRepository.save(convertToEntity(player)));
    }

    public PlayerDto updatePlayer(String playerId, PlayerDto.PlayerUpdateDto player) throws W2PEntityNotFoundException {
        if (!playerRepository.existsById(playerId)) {
            throw new W2PEntityNotFoundException(String.format("Player #%s not found", playerId));
        }
        return convertToDto(playerRepository.save(new Player(playerId, player.getName())));
    }

    public void deletePlayer(String playerId) throws W2PEntityNotFoundException {
        if (!playerRepository.existsById(playerId)) {
            throw new W2PEntityNotFoundException(String.format("Player #%s not found", playerId));
        }
        playerRepository.deleteById(playerId);
    }

    private PlayerDto convertToDto(Player sport) {
        return mapper.map(sport, PlayerDto.class);
    }

    private List<PlayerDto> convertToDto(List<Player> sports) {
        return sports.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private Player convertToEntity(PlayerDto sport) {
        return mapper.map(sport, Player.class);
    }

    private List<Player> convertToEntity(List<PlayerDto> sports) {
        return sports.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

}
