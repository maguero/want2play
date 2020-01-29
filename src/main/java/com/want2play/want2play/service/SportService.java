package com.want2play.want2play.service;

import com.want2play.want2play.dto.SportDto;

import java.util.List;

public interface SportService {

    List<SportDto> getAllSports();

    List<SportDto> getSportsByName(String name);

    SportDto getSportById(String id);

    SportDto insertSport(SportDto sport);

    SportDto updateSport(SportDto sport);

    void deleteSport(String id);

}
