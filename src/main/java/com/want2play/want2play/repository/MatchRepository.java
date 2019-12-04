package com.want2play.want2play.repository;

import com.want2play.want2play.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MatchRepository extends MongoRepository<Match, String> {

    List<Match> findByAdminPlayerId(String adminPlayerId);

    List<Match> findByState(String state);

    List<Match> findByStateAndStadiumCityAndSportId(String state, String city, String sportId);

}
