package com.ubuntu.ubuntu_app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import com.cloudinary.Cloudinary;

@Lazy
@Configuration
public class CloudinaryConfiguration {

    @Value("${cloudinary.url-config}")
    private String CLOUDINARY_URL_CONFIG;
    
    @Bean
    public Cloudinary getInstance() {
        return new Cloudinary(CLOUDINARY_URL_CONFIG);
    }
}
