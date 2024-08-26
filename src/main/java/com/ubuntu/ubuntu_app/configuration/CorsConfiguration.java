package com.ubuntu.ubuntu_app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfiguration implements WebMvcConfigurer{

    @Value("${cors.vercel}")
    private String vercelIp;    
    @Value("${cors.koyeb}")
    private String koyebIp;
    @Value("${cors.local}")
    private String localFrontend;
    
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(vercelIp, koyebIp, localFrontend)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Status", "Registration", "Login")
                .maxAge(3600)
                .allowCredentials(true);
    }
}
