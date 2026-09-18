package com.ubuntu.ubuntu_app.infrastructure.auth.web;

import com.ubuntu.ubuntu_app.application.auth.port.in.RefreshTokenUseCase;
import com.ubuntu.ubuntu_app.application.user.UserRole;
import com.ubuntu.ubuntu_app.application.user.api.AuthUserResponse;
import com.ubuntu.ubuntu_app.application.user.api.GoogleOidcProfile;
import com.ubuntu.ubuntu_app.application.user.port.in.OAuthLoginResult;
import com.ubuntu.ubuntu_app.application.user.port.in.OAuthLoginUseCase;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;
import com.ubuntu.ubuntu_app.shared.config.SecurityProperties;
import com.ubuntu.ubuntu_app.shared.config.TokenProperties;
import com.ubuntu.ubuntu_app.shared.security.GoogleTokenVerifier;
import com.ubuntu.ubuntu_app.shared.security.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthV1ControllerTest {

    @Mock
    private RefreshTokenUseCase refreshTokenUseCase;
    @Mock
    private OAuthLoginUseCase oAuthLoginUseCase;
    @Mock
    private GoogleTokenVerifier googleTokenVerifier;
    @Mock
    private JWTUtils jwtUtils;
    @Mock
    private UserRepository userRepository;

    private AuthV1Controller controller;

    @BeforeEach
    void setUp() {
        SecurityProperties securityProperties = new SecurityProperties(
                new SecurityProperties.Google("http://localhost:5173"),
                new SecurityProperties.Jwt("ubuntu_jwt", "ubuntu_refresh")
        );
        TokenProperties tokenProperties = new TokenProperties(15, 10080L);
        AuthResponseHelper authResponseHelper = new AuthResponseHelper(securityProperties, tokenProperties);
        controller = new AuthV1Controller(
                refreshTokenUseCase,
                securityProperties,
                tokenProperties,
                oAuthLoginUseCase,
                googleTokenVerifier,
                jwtUtils,
                userRepository,
                authResponseHelper
        );
    }

    @Test
    void loginWithMissingHeaderReturnsBadRequest() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseEntity<?> result = controller.login(null, null, response);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Missing token", response.getHeader("Status"));
    }

    @Test
    void loginWithInvalidHeaderFormatReturnsBadRequest() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseEntity<?> result = controller.login(null, "InvalidToken", response);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Missing token", response.getHeader("Status"));
    }

    @Test
    void loginWithValidLocalTokenReturnsOkWithHeadersAndCookies() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        UserEntity user = new UserEntity();
        user.setEmail("admin@ubuntu.com");

        when(jwtUtils.validateLocal("valid-local-token")).thenReturn("admin@ubuntu.com");
        when(userRepository.findByEmail("admin@ubuntu.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generate(user, 15)).thenReturn("new-local-jwt");
        when(refreshTokenUseCase.issueForEmail("admin@ubuntu.com", 15, 10080L))
                .thenReturn(new RefreshTokenUseCase.TokenPair("new-local-jwt", "refresh-uuid"));

        ResponseEntity<ApiResponse<AuthUserResponse>> result =
                controller.login(null, "Bearer valid-local-token", response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getHeaders().getFirst("Authorization"));
        assertEquals("Not required", result.getHeaders().getFirst("Registration"));
        assertEquals("Authorized", result.getHeaders().getFirst("Status"));
        assertNotNull(result.getHeaders().getFirst("Set-Cookie"));
        assertNotNull(result.getBody());
        assertEquals("admin@ubuntu.com", result.getBody().data().email());
    }

    @Test
    void loginWithValidGoogleTokenReturnsOkWithHeadersAndCookies() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        GoogleOidcProfile profile = new GoogleOidcProfile(
                "test@google.com", "Juan", "Perez", "https://img.com/photo.jpg", true
        );
        UserEntity user = new UserEntity();
        user.setEmail("test@google.com");

        when(jwtUtils.validateLocal("google-id-token")).thenThrow(new RuntimeException("Not local"));
        when(googleTokenVerifier.verify("google-id-token")).thenReturn(Optional.of(profile));
        when(oAuthLoginUseCase.login(profile, 15)).thenReturn(new OAuthLoginResult("generated-jwt", true));
        when(refreshTokenUseCase.issueForEmail("test@google.com", 15, 10080L))
                .thenReturn(new RefreshTokenUseCase.TokenPair("generated-jwt", "refresh-uuid"));
        when(userRepository.findByEmail("test@google.com")).thenReturn(Optional.of(user));

        ResponseEntity<ApiResponse<AuthUserResponse>> result =
                controller.login(new LoginTokenRequest("google-id-token"), null, response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getHeaders().getFirst("Authorization"));
        assertEquals("Registered", result.getHeaders().getFirst("Registration"));
        assertEquals("Authorized", result.getHeaders().getFirst("Status"));
        assertNotNull(result.getBody());
        assertEquals("test@google.com", result.getBody().data().email());
    }

    @Test
    void loginWithInvalidTokenReturnsUnauthorized() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateLocal("bad-token")).thenThrow(new RuntimeException("Not local"));
        when(googleTokenVerifier.verify("bad-token")).thenReturn(Optional.empty());

        ResponseEntity<?> result = controller.login(null, "Bearer bad-token", response);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Unauthorized", response.getHeader("Status"));
    }

    @Test
    void meReturnsCurrentUser() {
        UserEntity user = new UserEntity();
        user.setEmail("admin@ubuntu.com");
        user.setFirstName("Admin");
        user.setLastName("Ubuntu");
        user.setRole(UserRole.ADMIN);

        ResponseEntity<ApiResponse<AuthUserResponse>> response = controller.me(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("admin@ubuntu.com", response.getBody().data().email());
        assertEquals("Admin", response.getBody().data().firstName());
    }

    @Test
    void meWithoutUserReturnsUnauthorized() {
        ResponseEntity<ApiResponse<AuthUserResponse>> response = controller.me(null);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
