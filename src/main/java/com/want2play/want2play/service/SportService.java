package com.want2play.want2play.service;

import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PNotFoundException;
import com.want2play.want2play.model.Sport;
import com.want2play.want2play.repository.SportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SportService {

    private SportRepository sportRepository;

    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    public List<Sport> getAllSports() {
        return sportRepository.findAll();
    }

    public List<Sport> getSportsByName(String name) {
        return sportRepository.findByNameContaining(name);
    }

    public Sport getSportById(String id) {
        Optional<Sport> sportById = sportRepository.findById(id);
        if (sportById.isEmpty()) {
            throw new W2PNotFoundException();
        }
        return sportById.get();
    }

    public Sport insertSport(Sport sport) {
        if (sportRepository.existsById(sport.getId())) {
            throw new W2PEntityExistsException();
        }
        return sportRepository.save(sport);
    }

    public Sport updateSport(Sport sport) {
        checkIfSportExistsOrThrowException(sport.getId());
        return sportRepository.save(sport);
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

}
