package com.ubuntu.ubuntu_app.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.Cloudinary;
import com.ubuntu.ubuntu_app.shared.config.CloudinaryProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfiguration {

    private final CloudinaryProperties cloudinaryProperties;

    @Bean
    public Cloudinary getInstance() {
        return new Cloudinary(cloudinaryProperties.urlConfig());
    }

    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
