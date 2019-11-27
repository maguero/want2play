package com.want2play.want2play.repository;

import com.want2play.want2play.model.Player;
import com.want2play.want2play.model.Stadium;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String> {

}
