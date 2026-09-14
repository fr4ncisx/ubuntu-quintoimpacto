package com.ubuntu.ubuntu_app.configuration;

import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CosineSimilarityConfig {
    
    @Bean
    CosineSimilarity cosineSimilarity(){
        return new CosineSimilarity();
    }
}
