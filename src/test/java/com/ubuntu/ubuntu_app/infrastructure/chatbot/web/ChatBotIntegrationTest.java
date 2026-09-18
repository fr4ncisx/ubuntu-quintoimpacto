package com.ubuntu.ubuntu_app.infrastructure.chatbot.web;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.flyway.enabled=true",
        "app.seeding.enabled=true"
})
class ChatBotIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void postgresContainerIsRunning() {
        assertTrue(postgres.isRunning());
    }

    @Test
    void queryWithStemmingResolvesAnswerFromDatabase() throws Exception {
        mockMvc.perform(get("/api/v1/chatbot/answers").param("question", "como registrarse en la plataforma"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.Respuesta").exists());
    }

    @Test
    void queryWithAccentsResolvesAnswerFromDatabase() throws Exception {
        mockMvc.perform(get("/api/v1/chatbot/answers").param("question", "¿información sobre inversión?"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void unmappedQueryReturnsPoliteFallbackWithoutHallucination() throws Exception {
        mockMvc.perform(get("/api/v1/chatbot/answers").param("question", "receta de cocina extraterrestre"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.Respuesta").value("Lo siento, no pude comprender tu pregunta."));
    }
}
