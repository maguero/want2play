package com.want2play.want2play.service;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
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

    public Stadium getStadiumById(String id) throws W2PEntityNotFoundException {
        return stadiumRepository.findById(id)
                .orElseThrow(() -> new W2PEntityNotFoundException(String.format("Stadium #%s not found.", id)));
    }

    public List<Stadium> getStadiumsByCity(String city) {
        return stadiumRepository.findByCity(city);
    }

    public Stadium insertStadium(Stadium stadium) throws W2PEntityExistsException {
        if (stadiumRepository.existsById(stadium.getId())) {
            throw new W2PEntityExistsException(String.format("Stadium #%s already exists.", stadium.getId()));
        }
        return stadiumRepository.save(stadium);
    }

    public Stadium updateStadium(Stadium stadium) throws W2PEntityNotFoundException {
        checkIfStadiumExistsOrThrowException(stadium.getId());
        return stadiumRepository.save(stadium);
    }

    public void deleteStadium(String id) throws W2PEntityNotFoundException {
        checkIfStadiumExistsOrThrowException(id);
        stadiumRepository.deleteById(id);
    }

    private void checkIfStadiumExistsOrThrowException(String stadiumId) throws W2PEntityNotFoundException {
        if (!stadiumRepository.existsById(stadiumId)) {
            throw new W2PEntityNotFoundException(String.format("Stadium #%s not found.", stadiumId));
        }
    }
}
