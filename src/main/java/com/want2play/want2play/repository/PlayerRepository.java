package com.want2play.want2play.repository;

import com.want2play.want2play.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String> {

    List<Player> findByNameContaining(String name);

}
