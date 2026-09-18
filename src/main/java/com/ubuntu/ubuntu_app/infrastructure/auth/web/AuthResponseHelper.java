package com.ubuntu.ubuntu_app.infrastructure.auth.web;

import com.ubuntu.ubuntu_app.application.auth.port.in.RefreshTokenUseCase;
import com.ubuntu.ubuntu_app.application.user.api.AuthUserResponse;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;
import com.ubuntu.ubuntu_app.shared.config.SecurityProperties;
import com.ubuntu.ubuntu_app.shared.config.TokenProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AuthResponseHelper {

    private static final String HEADER_STATUS = "Status";
    private static final String HEADER_REGISTRATION = "Registration";

    private final SecurityProperties securityProperties;
    private final TokenProperties tokenProperties;

    public AuthResponseHelper(SecurityProperties securityProperties, TokenProperties tokenProperties) {
        this.securityProperties = securityProperties;
        this.tokenProperties = tokenProperties;
    }

    public ResponseEntity<ApiResponse<AuthUserResponse>> buildSuccessAuthResponse(
            UserEntity user, String registrationStatus, RefreshTokenUseCase.TokenPair pair) {
        ResponseCookie accessCookie = ResponseCookie.from(securityProperties.jwt().cookieName(), pair.accessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofSeconds((long) tokenProperties.expiration() * 60))
                .sameSite("Lax")
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from(securityProperties.jwt().refreshCookieName(), pair.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth")
                .maxAge(Duration.ofSeconds((long) tokenProperties.refreshExpiration() * 60))
                .sameSite("Lax")
                .build();

        AuthUserResponse authUser = new AuthUserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getPhone(),
                user.getImage(),
                user.getSubscribed());

        return ResponseEntity.ok()
                .header(HEADER_REGISTRATION, registrationStatus)
                .header(HEADER_STATUS, "Authorized")
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.ok(authUser));
    }
}
