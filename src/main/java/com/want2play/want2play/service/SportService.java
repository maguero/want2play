package com.want2play.want2play.service;

import com.want2play.want2play.dto.SportDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;

import java.util.List;

public interface SportService {

    List<SportDto> getAllSports();

    List<SportDto> getSportsByName(String name);

    SportDto getSportById(String id) throws W2PEntityNotFoundException;

    SportDto insertSport(SportDto sport) throws W2PEntityExistsException;

    SportDto updateSport(String id, SportDto.SportUpdateDto sport) throws W2PEntityNotFoundException;

    void deleteSport(String id) throws W2PEntityNotFoundException;

}
