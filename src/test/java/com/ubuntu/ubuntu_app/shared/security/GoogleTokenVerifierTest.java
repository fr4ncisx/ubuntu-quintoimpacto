package com.ubuntu.ubuntu_app.shared.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ubuntu.ubuntu_app.application.user.api.GoogleOidcProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleTokenVerifierTest {

    @Mock
    private JwtDecoder jwtDecoder;

    private GoogleTokenVerifier verifier;

    @BeforeEach
    void setUp() {
        verifier = new GoogleTokenVerifier(jwtDecoder);
    }

    @Test
    void nullOrBlankReturnsEmpty() {
        assertTrue(verifier.verify(null).isEmpty());
        assertTrue(verifier.verify("").isEmpty());
        assertTrue(verifier.verify("   ").isEmpty());
    }

    @Test
    void nonGoogleIssuerReturnsEmpty() {
        String token = JWT.create()
                .withIssuer("https://auth.example.com")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.none());

        assertTrue(verifier.verify(token).isEmpty());
    }

    @Test
    void expiredGoogleTokenReturnsEmpty() {
        String token = JWT.create()
                .withIssuer("https://accounts.google.com")
                .withExpiresAt(new Date(System.currentTimeMillis() - 60000))
                .sign(Algorithm.none());

        assertTrue(verifier.verify(token).isEmpty());
    }

    @Test
    void validGoogleTokenWithVerifiedEmailReturnsProfile() {
        String token = JWT.create()
                .withIssuer("https://accounts.google.com")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.none());

        Jwt jwt = new Jwt(
                token,
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of("alg", "none"),
                Map.of(
                        "email", "developer@gmail.com",
                        "email_verified", true,
                        "given_name", "Dev",
                        "family_name", "User",
                        "picture", "https://avatar.com/pic.png"
                )
        );

        when(jwtDecoder.decode(token)).thenReturn(jwt);

        Optional<GoogleOidcProfile> profileOpt = verifier.verify(token);

        assertTrue(profileOpt.isPresent());
        GoogleOidcProfile profile = profileOpt.get();
        assertEquals("developer@gmail.com", profile.email());
        assertEquals("Dev", profile.givenName());
        assertEquals("User", profile.familyName());
        assertEquals("https://avatar.com/pic.png", profile.picture());
        assertTrue(profile.emailVerified());
    }

    @Test
    void unverifiedEmailReturnsEmpty() {
        String token = JWT.create()
                .withIssuer("https://accounts.google.com")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.none());

        Jwt jwt = new Jwt(
                token,
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of("alg", "none"),
                Map.of(
                        "email", "unverified@gmail.com",
                        "email_verified", false
                )
        );

        when(jwtDecoder.decode(token)).thenReturn(jwt);

        assertTrue(verifier.verify(token).isEmpty());
    }

    @Test
    void tokenWithMismatchedAudienceReturnsEmpty() {
        GoogleTokenVerifier audienceVerifier = new GoogleTokenVerifier(jwtDecoder, "expected-client-id");
        String token = JWT.create()
                .withIssuer("https://accounts.google.com")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.none());

        Jwt jwt = new Jwt(
                token,
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of("alg", "none"),
                Map.of(
                        "aud", java.util.List.of("foreign-client-id"),
                        "email", "developer@gmail.com",
                        "email_verified", true
                )
        );

        when(jwtDecoder.decode(token)).thenReturn(jwt);

        assertTrue(audienceVerifier.verify(token).isEmpty());
    }

    @Test
    void tokenWithMatchingAudienceReturnsProfile() {
        GoogleTokenVerifier audienceVerifier = new GoogleTokenVerifier(jwtDecoder, "expected-client-id");
        String token = JWT.create()
                .withIssuer("https://accounts.google.com")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.none());

        Jwt jwt = new Jwt(
                token,
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of("alg", "none"),
                Map.of(
                        "aud", java.util.List.of("expected-client-id"),
                        "email", "developer@gmail.com",
                        "email_verified", true,
                        "given_name", "Dev",
                        "family_name", "User",
                        "picture", "https://avatar.com/pic.png"
                )
        );

        when(jwtDecoder.decode(token)).thenReturn(jwt);

        var profileOpt = audienceVerifier.verify(token);
        assertTrue(profileOpt.isPresent());
        assertEquals("developer@gmail.com", profileOpt.get().email());
    }
}
