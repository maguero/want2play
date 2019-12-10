package com.want2play.want2play.service;

import com.want2play.want2play.model.Stadium;
import com.want2play.want2play.repository.StadiumRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StadiumService {

    private StadiumRepository stadiumRepository;

    public StadiumService(StadiumRepository stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }

    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }

    public List<Stadium> getStadiumsByCity(String city) {
        return stadiumRepository.findByCity(city);
    }

    public Stadium saveStadium(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }

}
