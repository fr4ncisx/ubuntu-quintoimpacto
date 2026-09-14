package com.ubuntu.ubuntu_app.shared.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.shared.config.JwtProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTUtils {

    private final JwtProperties jwtProperties;

    private static final String TOKEN_ISSUER = "Ubuntu Application";

    public String generate(UserEntity user, int expirationTime) {
		return JWT.create()
            .withIssuer(TOKEN_ISSUER)
            .withSubject(user.getEmail())
            .withIssuedAt(getCurrentTime())
            .withExpiresAt(expirationTime(expirationTime))
            .withClaim("firstName", user.getFirstName())
            .withClaim("lastName", user.getLastName())
            .withClaim("phone", user.getPhone())
            .withClaim("role", user.getRole().name())
            .withClaim("image", user.getImage())
            .withClaim("newsletter", user.getSubscribed())
            .withClaim("vencimiento", LocalDateTime.ofInstant(expirationTime(expirationTime), ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
            .withJWTId(UUID.randomUUID().toString()).sign(getAlgorithm());
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(jwtProperties.secret().key());
    }

    private Instant getCurrentTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
    }

    private Instant expirationTime(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant();
    }

    public String validateLocal(String token) {
        JWTVerifier verifier =
            JWT.require(getAlgorithm())
            .withIssuer(TOKEN_ISSUER)
            .build();
        return verifier.verify(token).getSubject();
    }
}
