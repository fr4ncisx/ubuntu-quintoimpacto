package com.ubuntu.ubuntu_app.shared.security;

import java.time.Instant;
import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ubuntu.ubuntu_app.application.user.api.GoogleOidcProfile;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GoogleTokenVerifier {

    private final JwtDecoder jwtDecoder;
    private final String expectedClientId;

    public GoogleTokenVerifier() {
        this("");
    }

    @Autowired
    public GoogleTokenVerifier(
            @Value("${spring.security.oauth2.client.registration.google.client-id:}") String expectedClientId) {
        this.jwtDecoder = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
        this.expectedClientId = expectedClientId;
    }

    public GoogleTokenVerifier(JwtDecoder jwtDecoder, String expectedClientId) {
        this.jwtDecoder = jwtDecoder;
        this.expectedClientId = expectedClientId;
    }

    public GoogleTokenVerifier(JwtDecoder jwtDecoder) {
        this(jwtDecoder, null);
    }

    public Optional<GoogleOidcProfile> verify(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            DecodedJWT decoded = JWT.decode(token);
            String issuer = decoded.getIssuer();
            if (issuer == null || (!issuer.equals("https://accounts.google.com") && !issuer.equals("accounts.google.com"))) {
                return Optional.empty();
            }
            if (decoded.getExpiresAt() != null && decoded.getExpiresAt().toInstant().isBefore(Instant.now())) {
                return Optional.empty();
            }
            try {
                Jwt jwt = jwtDecoder.decode(token);
                if (expectedClientId != null && !expectedClientId.isBlank()) {
                    List<String> audience = jwt.getAudience();
                    if (audience == null || !audience.contains(expectedClientId)) {
                        log.warn("Google ID token audience mismatch: expected={}, received={}", expectedClientId, audience);
                        return Optional.empty();
                    }
                }
                String email = jwt.getClaimAsString("email");
                Boolean emailVerified = jwt.getClaimAsBoolean("email_verified");
                if (email == null || !Boolean.TRUE.equals(emailVerified)) {
                    return Optional.empty();
                }
                String givenName = jwt.getClaimAsString("given_name");
                String familyName = jwt.getClaimAsString("family_name");
                String picture = jwt.getClaimAsString("picture");
                return Optional.of(new GoogleOidcProfile(email, givenName, familyName, picture, emailVerified));
            } catch (Exception e) {
                log.warn("Google token verification error: {}", e.getMessage());
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
