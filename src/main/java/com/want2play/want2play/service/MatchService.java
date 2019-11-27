package com.want2play.want2play.service;

import com.want2play.want2play.model.Match;
import com.want2play.want2play.model.MatchStates;
import com.want2play.want2play.repository.MatchRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Match saveMatch(Match match) {
        if (StringUtils.isBlank(match.getId())) {
            match.setId(UUID.randomUUID().toString());
        }
        if (match.getState() == null) {
            match.setState(MatchStates.NEW);
        }
        return matchRepository.save(match);
    }
}
