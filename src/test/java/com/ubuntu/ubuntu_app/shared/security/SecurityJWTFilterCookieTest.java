package com.ubuntu.ubuntu_app.shared.security;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ubuntu.ubuntu_app.application.user.UserRole;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;
import com.ubuntu.ubuntu_app.shared.config.JwtProperties;
import com.ubuntu.ubuntu_app.shared.config.SecurityProperties;

import jakarta.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;

class SecurityJWTFilterCookieTest {

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private JWTUtils jwtUtilsWithSecret(String secret) {
        return new JWTUtils(new JwtProperties(new JwtProperties.Secret(secret)));
    }

    private SecurityJWTFilter filterWithSecret(String secret, UserRepository userRepository) {
        return new SecurityJWTFilter(jwtUtilsWithSecret(secret), userRepository,
                new SecurityProperties(
                        new SecurityProperties.Google("http://localhost:5173"),
                        new SecurityProperties.Jwt("ubuntu_jwt", "ubuntu_refresh")));
    }

    private UserEntity user() {
        return new UserEntity("Nombre", "Apellido", "me@mail.com",
                UserRole.USER, "123", null);
    }

    @Test
    void cookieWithValidTokenAuthenticates() throws Exception {
        UserRepository repo = Mockito.mock(UserRepository.class);
        Mockito.when(repo.findByEmail("me@mail.com")).thenReturn(Optional.of(user()));
        var filter = filterWithSecret("0123456789abcdef0123456789abcdef", repo);
        String token = jwtUtilsWithSecret("0123456789abcdef0123456789abcdef").generate(user(), 240);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users/7");
        request.setCookies(new Cookie("ubuntu_jwt", token));
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNotNull(chain.getRequest());
        assertEquals(200, response.getStatus());
    }

    @Test
    void bearerHeaderWithoutCookieIsForbidden() throws Exception {
        UserRepository repo = Mockito.mock(UserRepository.class);
        var filter = filterWithSecret("0123456789abcdef0123456789abcdef", repo);
        String token = jwtUtilsWithSecret("0123456789abcdef0123456789abcdef").generate(user(), 240);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users/7");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(403, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void missingTokenOnProtectedEndpointIsForbidden() throws Exception {
        UserRepository repo = Mockito.mock(UserRepository.class);
        var filter = filterWithSecret("0123456789abcdef0123456789abcdef", repo);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users/7");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(403, response.getStatus());
    }

    @Test
    void invalidCookieTokenIsUnauthorized() throws Exception {
        UserRepository repo = Mockito.mock(UserRepository.class);
        var filter = filterWithSecret("0123456789abcdef0123456789abcdef", repo);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users/7");
        request.setCookies(new Cookie("ubuntu_jwt", "no-es-un-token"));
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(401, response.getStatus());
    }

    @Test
    void invalidTokenIsLogged() throws Exception {
        var logger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(SecurityJWTFilter.class);
        var appender = new ch.qos.logback.core.read.ListAppender<ch.qos.logback.classic.spi.ILoggingEvent>();
        appender.start();
        logger.addAppender(appender);

        try {
            UserRepository repo = Mockito.mock(UserRepository.class);
            var filter = filterWithSecret("0123456789abcdef0123456789abcdef", repo);

            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users/7");
            request.setCookies(new Cookie("ubuntu_jwt", "no-es-un-token"));
            filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());
        } finally {
            logger.detachAppender(appender);
        }

        assertTrue(appender.list.stream()
                .anyMatch(e -> e.getLevel() == ch.qos.logback.classic.Level.WARN
                        && e.getFormattedMessage().contains("/api/v1/users/7")));
    }

    @Test
    void expiredTokenOnPublicEndpointPassesThrough() throws Exception {
        UserRepository repo = Mockito.mock(UserRepository.class);
        var filter = filterWithSecret("0123456789abcdef0123456789abcdef", repo);

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/auth/refresh");
        request.setCookies(new Cookie("ubuntu_jwt", "no-es-un-token"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNotNull(chain.getRequest());
        assertEquals(200, response.getStatus());
    }
}
