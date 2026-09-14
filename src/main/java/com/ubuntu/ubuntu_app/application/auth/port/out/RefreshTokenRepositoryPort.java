package com.ubuntu.ubuntu_app.application.auth.port.out;

import java.util.List;
import java.util.Optional;

import com.ubuntu.ubuntu_app.infrastructure.auth.entity.RefreshTokenEntity;

public interface RefreshTokenRepositoryPort {

    RefreshTokenEntity save(RefreshTokenEntity token);

    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    List<RefreshTokenEntity> findActiveByUserEmail(String userEmail);
}
