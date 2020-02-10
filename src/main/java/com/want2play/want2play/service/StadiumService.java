package com.want2play.want2play.service;

import com.want2play.want2play.dto.FieldDto;
import com.want2play.want2play.dto.StadiumDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;

import java.util.List;

public interface StadiumService {

    List<StadiumDto> getAllStadiums();

    StadiumDto getStadiumById(String id) throws W2PEntityNotFoundException;

    List<StadiumDto> getStadiumsByCity(String city);

    StadiumDto insertStadium(StadiumDto stadium) throws W2PEntityExistsException;

    StadiumDto updateStadium(String id, StadiumDto stadium) throws W2PEntityNotFoundException;

    void deleteStadium(String id) throws W2PEntityNotFoundException;

    StadiumDto addStadiumField(String stadiumId, FieldDto field) throws W2PEntityNotFoundException, W2PEntityExistsException;

}