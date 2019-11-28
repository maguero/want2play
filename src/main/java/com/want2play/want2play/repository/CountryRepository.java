package com.want2play.want2play.repository;

import com.want2play.want2play.model.Country;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CountryRepository extends MongoRepository<Country, String> {
    
}
