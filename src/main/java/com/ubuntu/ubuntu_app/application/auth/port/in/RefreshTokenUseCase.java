package com.ubuntu.ubuntu_app.application.auth.port.in;

import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;

public interface RefreshTokenUseCase {

    TokenPair issue(UserEntity user, int accessMinutes, long refreshMinutes);

    TokenPair issueForEmail(String userEmail, int accessMinutes, long refreshMinutes);

    TokenPair rotate(String rawRefreshToken, int accessMinutes, long refreshMinutes);

    void revoke(String rawRefreshToken);

    void revokeAll(String userEmail);

    record TokenPair(String accessToken, String refreshToken) {
    }
}
