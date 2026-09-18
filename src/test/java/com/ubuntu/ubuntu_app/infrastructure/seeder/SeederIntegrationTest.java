package com.ubuntu.ubuntu_app.infrastructure.seeder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.ubuntu.ubuntu_app.infrastructure.microbusiness.repository.MicrobusinessRepository;
import com.ubuntu.ubuntu_app.infrastructure.publication.repository.PublicationRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.flyway.enabled=true",
        "app.seeding.enabled=true"
})
class SeederIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MicrobusinessRepository microbusinessRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Test
    void postgresContainerIsRunning() {
        assertTrue(postgres.isRunning());
    }

    @Test
    void microbusinessesAreSeeded() {
        long count = microbusinessRepository.count();
        assertEquals(5, count);
    }

    @Test
    void publicationsAreSeeded() {
        long count = publicationRepository.count();
        assertTrue(count >= 6);
    }
}
