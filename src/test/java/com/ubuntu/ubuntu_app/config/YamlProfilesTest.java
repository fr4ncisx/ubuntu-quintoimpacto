package com.ubuntu.ubuntu_app.config;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.*;

class YamlProfilesTest {

    private Properties load(String file) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(new ClassPathResource(file));
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Test
    void baseProfileHasCommonKeys() {
        Properties props = load("application.yml");

        assertEquals("ubuntu_app", props.getProperty("spring.application.name"));
        assertNotNull(props.getProperty("jwt.secret.key"));
        assertNotNull(props.getProperty("spring.config.import"));
        assertNotNull(props.getProperty("spring.security.oauth2.client.registration.google.client-id"));
        assertNotNull(props.getProperty("app.security.jwt.cookie-name"));
    }

    @Test
    void devProfileValidatesAndSeeds() {
        Properties props = load("application-dev.yml");

        assertEquals("validate", props.getProperty("spring.jpa.hibernate.ddl-auto"));
        assertEquals("true", props.getProperty("app.seeding.enabled"));
    }

    @Test
    void prodProfileIsSafe() {
        Properties props = load("application-prod.yml");

        assertEquals("validate", props.getProperty("spring.jpa.hibernate.ddl-auto"));
        assertEquals("false", props.getProperty("app.seeding.enabled"));
    }

    @Test
    void testProfileDisablesSeeding() {
        Properties props = load("application-test.yml");

        assertEquals("false", props.getProperty("app.seeding.enabled"));
        assertNotNull(props.getProperty("SECRET_KEY"));
    }
}
