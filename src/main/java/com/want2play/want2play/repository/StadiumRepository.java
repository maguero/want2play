package com.want2play.want2play.repository;

import com.want2play.want2play.model.Stadium;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StadiumRepository extends MongoRepository<Stadium, String> {

    List<Stadium> findByCity(String city);

}
