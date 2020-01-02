package com.want2play.want2play.repository;

import com.want2play.want2play.model.Sport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SportRepository extends MongoRepository<Sport, String> {

    List<Sport> findByNameContaining(String name);

}
