package com.ubuntu.ubuntu_app.application.auth.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import com.ubuntu.ubuntu_app.application.auth.port.out.RefreshTokenRepositoryPort;
import com.ubuntu.ubuntu_app.application.user.UserRole;
import com.ubuntu.ubuntu_app.infrastructure.auth.entity.RefreshTokenEntity;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;
import com.ubuntu.ubuntu_app.shared.security.JWTUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepositoryPort repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTUtils jwtUtils;

    @InjectMocks
    private RefreshTokenService service;

    private UserEntity user() {
        return new UserEntity("Nombre", "Apellido", "me@mail.com", UserRole.USER, "123", null);
    }

    private RefreshTokenEntity stored(boolean revoked, Instant expiresAt) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(1L);
        entity.setTokenHash("hash");
        entity.setUserEmail("me@mail.com");
        entity.setExpiresAt(expiresAt);
        entity.setRevoked(revoked);
        entity.setCreatedAt(Instant.now().minusSeconds(60));
        return entity;
    }

    @Test
    void issuePersistsHashAndReturnsPair() {
        Mockito.when(jwtUtils.generate(any(), Mockito.eq(15))).thenReturn("access");

        var pair = service.issue(user(), 15, 10080);

        assertEquals("access", pair.accessToken());
        assertNotNull(pair.refreshToken());
        assertEquals(64, pair.refreshToken().length());
        var captor = org.mockito.ArgumentCaptor.forClass(RefreshTokenEntity.class);
        Mockito.verify(repository).save(captor.capture());
        assertNotEquals(pair.refreshToken(), captor.getValue().getTokenHash());
        assertEquals(64, captor.getValue().getTokenHash().length());
        assertFalse(captor.getValue().isRevoked());
    }

    @Test
    void rotateRevokesOldAndIssuesNew() {
        Mockito.when(repository.findByTokenHash(any()))
                .thenReturn(Optional.of(stored(false, Instant.now().plusSeconds(600))));
        Mockito.when(userRepository.findByEmail("me@mail.com")).thenReturn(Optional.of(user()));
        Mockito.when(jwtUtils.generate(any(), Mockito.eq(15))).thenReturn("new-access");

        var pair = service.rotate("raw-old", 15, 10080);

        assertEquals("new-access", pair.accessToken());
        assertNotNull(pair.refreshToken());
        Mockito.verify(repository, Mockito.times(2)).save(any(RefreshTokenEntity.class));
    }

    @Test
    void rotateUnknownHashIsDenied() {
        Mockito.when(repository.findByTokenHash(any())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> service.rotate("unknown", 15, 10080));
    }

    @Test
    void rotateRevokedTokenRevokesAll() {
        Mockito.when(repository.findByTokenHash(any()))
                .thenReturn(Optional.of(stored(true, Instant.now().plusSeconds(600))));
        Mockito.when(repository.findActiveByUserEmail("me@mail.com")).thenReturn(List.of());

        assertThrows(BadCredentialsException.class, () -> service.rotate("reused", 15, 10080));
        Mockito.verify(repository).findActiveByUserEmail("me@mail.com");
    }

    @Test
    void rotateExpiredTokenIsDenied() {
        Mockito.when(repository.findByTokenHash(any()))
                .thenReturn(Optional.of(stored(false, Instant.now().minusSeconds(10))));

        assertThrows(BadCredentialsException.class, () -> service.rotate("expired", 15, 10080));
        Mockito.verify(repository).save(any(RefreshTokenEntity.class));
    }

    @Test
    void revokeMissingTokenIsNoOp() {
        Mockito.when(repository.findByTokenHash(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> service.revoke("missing"));
        Mockito.verify(repository, Mockito.never()).save(any(RefreshTokenEntity.class));
    }
}
