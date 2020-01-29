package com.want2play.want2play.service.impl;

import com.want2play.want2play.dto.SportDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PNotFoundException;
import com.want2play.want2play.model.Sport;
import com.want2play.want2play.repository.SportRepository;
import com.want2play.want2play.service.SportService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SportServiceImpl implements SportService {

    private SportRepository sportRepository;
    private ModelMapper mapper;

    public SportServiceImpl(SportRepository sportRepository, ModelMapper mapper) {
        this.sportRepository = sportRepository;
        this.mapper = mapper;
    }

    public List<SportDto> getAllSports() {
        return convertToDto(sportRepository.findAll());
    }

    public List<SportDto> getSportsByName(String name) {
        return convertToDto(
                sportRepository.findByNameContaining(name)
        );
    }

    public SportDto getSportById(String id) {
        return convertToDto(
                sportRepository.findById(id).orElseThrow(() -> new W2PNotFoundException())
        );
    }

    public SportDto insertSport(SportDto sport) {
        if (sportRepository.existsById(sport.getId())) {
            throw new W2PEntityExistsException();
        }
        return convertToDto(
                sportRepository.save(convertToEntity(sport))
        );
    }

    public SportDto updateSport(SportDto sport) {
        checkIfSportExistsOrThrowException(sport.getId());
        return convertToDto(
                sportRepository.save(convertToEntity(sport))
        );
    }

    public void deleteSport(String id) {
        checkIfSportExistsOrThrowException(id);
        sportRepository.deleteById(id);
    }

    private void checkIfSportExistsOrThrowException(String sportId) {
        if (!sportRepository.existsById(sportId)) {
            throw new W2PNotFoundException();
        }
    }

    private SportDto convertToDto(Sport sport) {
        return mapper.map(sport, SportDto.class);
    }

    private List<SportDto> convertToDto(List<Sport> sports) {
        return sports.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private Sport convertToEntity(SportDto sport) {
        return mapper.map(sport, Sport.class);
    }

    private List<Sport> convertToEntity(List<SportDto> sports) {
        return sports.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

}
