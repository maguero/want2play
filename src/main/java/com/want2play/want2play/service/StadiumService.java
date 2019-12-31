package com.want2play.want2play.service;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PNotFoundException;
import com.want2play.want2play.model.Stadium;
import com.want2play.want2play.repository.StadiumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StadiumService {

    private StadiumRepository stadiumRepository;

    public StadiumService(StadiumRepository stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }

    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }

    public Stadium getStadiumById(String id) {
        Optional<Stadium> stadiumById = stadiumRepository.findById(id);
        if (stadiumById.isEmpty()) {
            throw new W2PNotFoundException();
        }
        return stadiumById.get();
    }

    public List<Stadium> getStadiumsByCity(String city) {
        return stadiumRepository.findByCity(city);
    }

    public Stadium insertStadium(Stadium stadium) {
        if (stadiumRepository.existsById(stadium.getId())) {
            throw new W2PEntityExistsException();
        }
        return stadiumRepository.save(stadium);
    }

    public Stadium updateStadium(Stadium stadium) {
        checkIfStadiumExistsOrThrowException(stadium.getId());
        return stadiumRepository.save(stadium);
    }

    public void deleteStadium(String id) {
        checkIfStadiumExistsOrThrowException(id);
        stadiumRepository.deleteById(id);
    }

    private void checkIfStadiumExistsOrThrowException(String stadiumId) {
        if (!stadiumRepository.existsById(stadiumId)) {
            throw new W2PNotFoundException();
        }
    }
}
