package com.want2play.want2play.service;

import com.want2play.want2play.dto.MatchDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;

import java.util.List;

public interface MatchService {

    List<MatchDto> getAllMatches();

    MatchDto getById(String id) throws W2PEntityNotFoundException;

    MatchDto insertMatch(MatchDto match) throws W2PEntityExistsException;

    MatchDto updateMatch(MatchDto match);

    void deleteMatch(String matchId) throws W2PEntityNotFoundException;

    List<MatchDto> getMatchesByAdminPlayer(String adminPlayerId);

    List<MatchDto> getMatchesByState(String state);

    List<MatchDto> getOpenMatchesByCityAndSport(String city, String sportId);

}
