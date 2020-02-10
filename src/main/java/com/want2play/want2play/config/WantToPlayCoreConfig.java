package com.want2play.want2play.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WantToPlayCoreConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
