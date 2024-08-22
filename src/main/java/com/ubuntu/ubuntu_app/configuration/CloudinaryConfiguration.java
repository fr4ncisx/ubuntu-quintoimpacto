package com.ubuntu.ubuntu_app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;

@Lazy
@Component
public class CloudinaryConfiguration {

    @Value("${cloudinary.url-config}")
    private String CLOUDINARY_URL_CONFIG;
    
    public Cloudinary getInstance() {
        return new Cloudinary(CLOUDINARY_URL_CONFIG);
    }
}
