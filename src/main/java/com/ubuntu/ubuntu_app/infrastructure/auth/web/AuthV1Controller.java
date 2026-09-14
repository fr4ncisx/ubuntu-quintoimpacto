package com.ubuntu.ubuntu_app.infrastructure.auth.web;

import com.ubuntu.ubuntu_app.application.auth.port.in.RefreshTokenUseCase;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;
import com.ubuntu.ubuntu_app.shared.config.SecurityProperties;
import com.ubuntu.ubuntu_app.shared.config.TokenProperties;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthV1Controller {

    private static final String HEADER_STATUS = "Status";

    private final RefreshTokenUseCase refreshTokenUseCase;
    private final SecurityProperties securityProperties;
    private final TokenProperties tokenProperties;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
            @RequestHeader(name = "Authorization", required = false) String header,
            HttpServletResponse response) {
        if (header == null) {
            response.addHeader(HEADER_STATUS, "Missing token");
            return ResponseEntity.badRequest().body(ApiResponse.message("Missing token", HttpStatus.BAD_REQUEST));
        }
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            response.addHeader(HEADER_STATUS, "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.message("Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        return ResponseEntity.ok(ApiResponse.message("Authorized", HttpStatus.OK));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "ubuntu_refresh", required = false) String refreshToken) {
        refreshTokenUseCase.revoke(refreshToken);
        ResponseCookie clearedAccess = ResponseCookie.from(securityProperties.jwt().cookieName(), "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ZERO)
                .sameSite("None")
                .build();
        ResponseCookie clearedRefresh = ResponseCookie.from(securityProperties.jwt().refreshCookieName(), "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth")
                .maxAge(Duration.ZERO)
                .sameSite("None")
                .build();
        return ResponseEntity.ok()
                .header("Set-Cookie", clearedAccess.toString())
                .header("Set-Cookie", clearedRefresh.toString())
                .body(ApiResponse.message("Sesión cerrada", HttpStatus.OK));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Void>> refresh(
            @CookieValue(name = "ubuntu_refresh", required = false) String refreshToken) {
        var pair = refreshTokenUseCase.rotate(refreshToken, tokenProperties.expiration(), tokenProperties.refreshExpiration());
        ResponseCookie access = ResponseCookie.from(securityProperties.jwt().cookieName(), pair.accessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofSeconds((long) tokenProperties.expiration() * 60))
                .sameSite("None")
                .build();
        ResponseCookie refresh = ResponseCookie.from(securityProperties.jwt().refreshCookieName(), pair.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth")
                .maxAge(Duration.ofSeconds(tokenProperties.refreshExpiration() * 60))
                .sameSite("None")
                .build();
        return ResponseEntity.ok()
                .header("Set-Cookie", access.toString())
                .header("Set-Cookie", refresh.toString())
                .body(ApiResponse.message("Tokens renovados", HttpStatus.OK));
    }
}
