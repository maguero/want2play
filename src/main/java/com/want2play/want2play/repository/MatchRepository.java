package com.want2play.want2play.repository;

import com.want2play.want2play.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {

}
