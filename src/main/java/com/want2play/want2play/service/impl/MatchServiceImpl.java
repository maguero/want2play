package com.want2play.want2play.service.impl;

import com.want2play.want2play.dto.MatchDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.model.Match;
import com.want2play.want2play.model.MatchStates;
import com.want2play.want2play.repository.MatchRepository;
import com.want2play.want2play.service.MatchService;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;
    private ModelMapper mapper;

    public MatchServiceImpl(MatchRepository matchRepository, ModelMapper mapper) {
        this.matchRepository = matchRepository;
        this.mapper = mapper;
    }

    public List<MatchDto> getAllMatches() {
        return convertToDto(matchRepository.findAll());
    }

    public MatchDto getById(String id) throws W2PEntityNotFoundException {
        return convertToDto(
                matchRepository.findById(id)
                        .orElseThrow(() -> new W2PEntityNotFoundException(String.format("Match #%s not found.", id)))
        );
    }

    public MatchDto insertMatch(MatchDto match) throws W2PEntityExistsException {
        if (StringUtils.isBlank(match.getId())) {
            match.setId(UUID.randomUUID().toString());
        }
        if (matchRepository.existsById(match.getId())) {
            throw new W2PEntityExistsException(String.format("Match #%s already exists.", match.getId()));
        }
        if (match.getState() == null) {
            match.setState(MatchStates.NEW);
        }

        return convertToDto(matchRepository.save(convertToEntity(match)));
    }

    public MatchDto updateMatch(MatchDto match) {
        // TODO implement
        throw new NotImplementedException();
    }

    public void deleteMatch(String matchId) throws W2PEntityNotFoundException {
        if (!matchRepository.existsById(matchId)) {
            throw new W2PEntityNotFoundException(String.format("Match #%s not found.", matchId));
        }
        matchRepository.deleteById(matchId);
    }

    public List<MatchDto> getMatchesByAdminPlayer(String adminPlayerId) {
        return convertToDto(matchRepository.findByAdminPlayerId(adminPlayerId));
    }

    public List<MatchDto> getMatchesByState(String state) {
        return convertToDto(matchRepository.findByState(state));
    }

    public List<MatchDto> getOpenMatchesByCityAndSport(String city, String sportId) {
        return convertToDto(
                matchRepository.findByStateAndStadiumCityAndSportId(MatchStates.OPEN.toString(), city, sportId)
        );
    }

    private MatchDto convertToDto(Match sport) {
        return mapper.map(sport, MatchDto.class);
    }

    private List<MatchDto> convertToDto(List<Match> sports) {
        return sports.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private Match convertToEntity(MatchDto sport) {
        return mapper.map(sport, Match.class);
    }

    private List<Match> convertToEntity(List<MatchDto> sports) {
        return sports.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

}
