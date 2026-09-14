package com.ubuntu.ubuntu_app.infrastructure.auth.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.auth.port.out.RefreshTokenRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.auth.entity.RefreshTokenEntity;
import com.ubuntu.ubuntu_app.infrastructure.auth.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenJpaAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenRepository repository;

    @Override
    public RefreshTokenEntity save(RefreshTokenEntity token) {
        return repository.save(token);
    }

    @Override
    public Optional<RefreshTokenEntity> findByTokenHash(String tokenHash) {
        return repository.findByTokenHash(tokenHash);
    }

    @Override
    public List<RefreshTokenEntity> findActiveByUserEmail(String userEmail) {
        return repository.findAllByUserEmailAndRevokedFalse(userEmail);
    }
}
