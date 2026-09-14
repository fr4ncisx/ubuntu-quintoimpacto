package com.ubuntu.ubuntu_app.application.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HexFormat;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.application.auth.port.in.RefreshTokenUseCase;
import com.ubuntu.ubuntu_app.application.auth.port.out.RefreshTokenRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.auth.entity.RefreshTokenEntity;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;
import com.ubuntu.ubuntu_app.shared.security.JWTUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenService implements RefreshTokenUseCase {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RefreshTokenRepositoryPort repository;
    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;

    @Override
    @Transactional
    public TokenPair issue(UserEntity user, int accessMinutes, long refreshMinutes) {
        String refreshToken = generateRawToken();
        var entity = new RefreshTokenEntity();
        entity.setTokenHash(hash(refreshToken));
        entity.setUserEmail(user.getEmail());
        entity.setExpiresAt(Instant.now().plusSeconds(refreshMinutes * 60));
        entity.setRevoked(false);
        entity.setCreatedAt(Instant.now());
        repository.save(entity);
        return new TokenPair(jwtUtils.generate(user, accessMinutes), refreshToken);
    }

    @Override
    @Transactional
    public TokenPair rotate(String rawRefreshToken, int accessMinutes, long refreshMinutes) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token ausente");
        }
        var stored = repository.findByTokenHash(hash(rawRefreshToken))
                .orElseThrow(() -> new BadCredentialsException("Refresh token inválido"));
        if (stored.isRevoked()) {
            revokeAll(stored.getUserEmail());
            throw new BadCredentialsException("Refresh token reutilizado: sesión revocada");
        }
        if (stored.getExpiresAt().isBefore(Instant.now())) {
            stored.setRevoked(true);
            repository.save(stored);
            throw new BadCredentialsException("Refresh token expirado");
        }
        stored.setRevoked(true);
        repository.save(stored);
        var user = userRepository.findByEmail(stored.getUserEmail())
                .orElseThrow(() -> new BadCredentialsException("Usuario inexistente"));
        String refreshToken = generateRawToken();
        var entity = new RefreshTokenEntity();
        entity.setTokenHash(hash(refreshToken));
        entity.setUserEmail(stored.getUserEmail());
        entity.setExpiresAt(Instant.now().plusSeconds(refreshMinutes * 60));
        entity.setRevoked(false);
        entity.setCreatedAt(Instant.now());
        repository.save(entity);
        return new TokenPair(jwtUtils.generate(user, accessMinutes), refreshToken);
    }

    @Override
    @Transactional
    public void revoke(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            return;
        }
        repository.findByTokenHash(hash(rawRefreshToken)).ifPresent(token -> {
            token.setRevoked(true);
            repository.save(token);
        });
    }

    @Override
    @Transactional
    public void revokeAll(String userEmail) {
        for (var token : repository.findActiveByUserEmail(userEmail)) {
            token.setRevoked(true);
            repository.save(token);
        }
    }

    @Override
    @Transactional
    public TokenPair issueForEmail(String userEmail, int accessMinutes, long refreshMinutes) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BadCredentialsException("Usuario inexistente"));
        return issue(user, accessMinutes, refreshMinutes);
    }

    private String generateRawToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(rawToken.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}
