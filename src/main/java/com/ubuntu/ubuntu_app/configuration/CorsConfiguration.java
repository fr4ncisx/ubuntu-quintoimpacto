package com.ubuntu.ubuntu_app.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ubuntu.ubuntu_app.shared.config.CorsProperties;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class CorsConfiguration implements WebMvcConfigurer{

    private final CorsProperties corsProperties;
    
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsProperties.vercel(), corsProperties.koyeb(), corsProperties.local())
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Status", "Registration", "Login")
                .maxAge(3600)
                .allowCredentials(true);
    }
}
