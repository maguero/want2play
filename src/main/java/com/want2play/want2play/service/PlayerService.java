package com.want2play.want2play.service;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PNotFoundException;
import com.want2play.want2play.model.Player;
import com.want2play.want2play.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getPlayersByName(String name) {
        return playerRepository.findByNameContaining(name);
    }

    public Player getPlayerById(String id) {
        Optional<Player> playerById = playerRepository.findById(id);
        if (playerById.isEmpty()) {
            throw new W2PNotFoundException();
        }
        return playerById.get();
    }

    public Player insertPlayer(Player player) {
        if (playerRepository.existsById(player.getId())) {
            throw new W2PEntityExistsException();
        }
        return playerRepository.save(player);
    }

    public Player updatePlayer(Player player) {
        Optional<Player> playerById = playerRepository.findById(player.getId());
        if (playerById.isEmpty()) {
            throw new W2PNotFoundException();
        }
        return playerRepository.save(player);
    }

    public void deletePlayer(String id) {
        Optional<Player> playerById = playerRepository.findById(id);
        if (playerById.isEmpty()) {
            throw new W2PNotFoundException();
        }
        playerRepository.delete(playerById.get());
    }

}
