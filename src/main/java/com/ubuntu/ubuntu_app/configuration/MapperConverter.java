package com.ubuntu.ubuntu_app.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConverter {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
