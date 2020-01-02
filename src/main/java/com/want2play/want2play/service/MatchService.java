package com.want2play.want2play.service;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PNotFoundException;
import com.want2play.want2play.model.Match;
import com.want2play.want2play.model.MatchStates;
import com.want2play.want2play.repository.MatchRepository;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MatchService {

    private MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Match getById(String id) {
        Optional<Match> matchById = matchRepository.findById(id);
        if (matchById.isEmpty()) {
            throw new W2PNotFoundException();
        }
        return matchById.get();
    }

    public Match saveMatch(Match match) {
        if (StringUtils.isBlank(match.getId())) {
            match.setId(UUID.randomUUID().toString());
        }
        if (matchRepository.existsById(match.getId())) {
            throw new W2PEntityExistsException();
        }
        if (match.getState() == null) {
            match.setState(MatchStates.NEW);
        }

        return matchRepository.save(match);
    }

    public Match updateMatch(Match match) {
        throw new NotImplementedException();
    }

    public void deleteMatch(String matchId) {
        Optional<Match> match = matchRepository.findById(matchId);
        if (match.isEmpty()) {
            throw new W2PNotFoundException();
        }
        matchRepository.delete(match.get());
    }

    public List<Match> getMatchesByAdminPlayer(String adminPlayerId) {
        return matchRepository.findByAdminPlayerId(adminPlayerId);
    }

    public List<Match> getMatchesByState(String state) {
        return matchRepository.findByState(state);
    }

    public List<Match> getOpenMatchesByCityAndSport(String city, String sportId) {
        return matchRepository.findByStateAndStadiumCityAndSportId(MatchStates.OPEN.toString(), city, sportId);
    }
}
