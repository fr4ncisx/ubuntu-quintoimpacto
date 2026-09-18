package com.ubuntu.ubuntu_app.infrastructure.auth;

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
import jakarta.servlet.http.Cookie;

import com.ubuntu.ubuntu_app.application.user.UserRole;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;
import com.ubuntu.ubuntu_app.shared.config.SecurityProperties;
import com.ubuntu.ubuntu_app.shared.security.JWTUtils;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.flyway.enabled=true",
        "app.seeding.enabled=true"
})
class AuthIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private SecurityProperties securityProperties;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void postgresContainerIsRunning() {
        assertTrue(postgres.isRunning());
    }

    @Test
    void protectedEndpointWithoutTokenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void protectedEndpointWithValidJwtCookieReturnsOk() throws Exception {
        UserEntity user = new UserEntity(
                "Integration",
                "Tester",
                "integration@test.com",
                UserRole.USER,
                "123456789",
                "img.jpg"
        );
        user = userRepository.save(user);

        String jwt = jwtUtils.generate(user, 15);
        Cookie authCookie = new Cookie(securityProperties.jwt().cookieName(), jwt);

        mockMvc.perform(get("/api/v1/auth/me")
                .cookie(authCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("integration@test.com"))
                .andExpect(jsonPath("$.data.firstName").value("Integration"));
    }
}
