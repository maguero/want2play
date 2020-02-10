package com.want2play.want2play.service.impl;

import com.want2play.want2play.dto.FieldDto;
import com.want2play.want2play.dto.StadiumDto;
import com.want2play.want2play.exception.W2PEntityExistsException;
import com.want2play.want2play.exception.W2PEntityNotFoundException;
import com.want2play.want2play.model.Field;
import com.want2play.want2play.model.Stadium;
import com.want2play.want2play.repository.StadiumRepository;
import com.want2play.want2play.service.StadiumService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StadiumServiceImpl implements StadiumService {

    private StadiumRepository stadiumRepository;
    private ModelMapper mapper;

    public StadiumServiceImpl(StadiumRepository stadiumRepository, ModelMapper mapper) {
        this.stadiumRepository = stadiumRepository;
        this.mapper = mapper;
    }

    public List<StadiumDto> getAllStadiums() {
        return convertToDto(stadiumRepository.findAll());
    }

    public StadiumDto getStadiumById(String id) throws W2PEntityNotFoundException {
        return convertToDto(
                stadiumRepository.findById(id)
                        .orElseThrow(() -> new W2PEntityNotFoundException(String.format("Stadium #%s not found.", id)))
        );
    }

    public List<StadiumDto> getStadiumsByCity(String city) {
        return convertToDto(stadiumRepository.findByCity(city));
    }

    public StadiumDto insertStadium(StadiumDto stadium) throws W2PEntityExistsException {
        if (Objects.isNull(stadium.getId())) {
            stadium.setId(UUID.randomUUID().toString());
        }
        if (stadiumRepository.existsById(stadium.getId())) {
            throw new W2PEntityExistsException(String.format("Stadium #%s already exists.", stadium.getId()));
        }
        return convertToDto(stadiumRepository.save(convertToEntity(stadium)));
    }

    public StadiumDto updateStadium(String id, StadiumDto stadium) throws W2PEntityNotFoundException {
        checkIfStadiumExistsOrThrowException(id);
        // TODO refactor if necesary
        return convertToDto(stadiumRepository.save(convertToEntity(stadium)));
    }

    public void deleteStadium(String id) throws W2PEntityNotFoundException {
        checkIfStadiumExistsOrThrowException(id);
        stadiumRepository.deleteById(id);
    }

    @Override
    public StadiumDto addStadiumField(String stadiumId, FieldDto field) throws W2PEntityNotFoundException, W2PEntityExistsException {
        Stadium stadium = stadiumRepository.findById(stadiumId).orElseThrow(
                () -> new W2PEntityNotFoundException(String.format("Stadium #%s not found.", stadiumId))
        );
        if (stadium.getFields().stream().anyMatch(f -> Objects.equals(f.getName(), field.getName()))) {
            throw new W2PEntityExistsException(String.format("Field $s for Stadium #%s already exists.", field.getName(), stadium.getId()));
        } else {
            stadium.getFields().add(mapper.map(field, Field.class));
        }
        return convertToDto(stadiumRepository.save(stadium));
    }

    private void checkIfStadiumExistsOrThrowException(String stadiumId) throws W2PEntityNotFoundException {
        if (!stadiumRepository.existsById(stadiumId)) {
            throw new W2PEntityNotFoundException(String.format("Stadium #%s not found.", stadiumId));
        }
    }

    private StadiumDto convertToDto(Stadium stadium) {
        return mapper.map(stadium, StadiumDto.class);
    }

    private List<StadiumDto> convertToDto(List<Stadium> stadiums) {
        return stadiums.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private Stadium convertToEntity(StadiumDto stadium) {
        return mapper.map(stadium, Stadium.class);
    }

    private List<Stadium> convertToEntity(List<StadiumDto> stadiums) {
        return stadiums.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

}
