package com.ubuntu.ubuntu_app.infrastructure.auth.web;

import com.ubuntu.ubuntu_app.application.auth.port.in.RefreshTokenUseCase;
import com.ubuntu.ubuntu_app.application.user.UserRole;
import com.ubuntu.ubuntu_app.application.user.api.AuthUserResponse;
import com.ubuntu.ubuntu_app.application.user.port.in.OAuthLoginUseCase;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;
import com.ubuntu.ubuntu_app.shared.config.SecurityProperties;
import com.ubuntu.ubuntu_app.shared.config.TokenProperties;
import com.ubuntu.ubuntu_app.shared.security.GoogleTokenVerifier;
import com.ubuntu.ubuntu_app.shared.security.JWTUtils;
import com.ubuntu.ubuntu_app.shared.support.RandomPhoneGenerator;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final OAuthLoginUseCase oAuthLoginUseCase;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final AuthResponseHelper authResponseHelper;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthUserResponse>> login(
            @RequestBody(required = false) LoginTokenRequest body,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String header,
            HttpServletResponse response) {
        String token = null;
        if (body != null && body.idToken() != null && !body.idToken().isBlank()) {
            token = body.idToken().trim();
        } else if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7).trim();
        }

        if (token == null || token.isEmpty()) {
            response.addHeader(HEADER_STATUS, "Missing token");
            return ResponseEntity.badRequest().body(ApiResponse.message("Missing token", HttpStatus.BAD_REQUEST));
        }

        try {
            String localEmail = jwtUtils.validateLocal(token);
            if (localEmail != null) {
                var userOpt = userRepository.findByEmail(localEmail);
                if (userOpt.isPresent()) {
                    UserEntity user = userOpt.get();
                    jwtUtils.generate(user, tokenProperties.expiration());
                    var pair = refreshTokenUseCase.issueForEmail(user.getEmail(), tokenProperties.expiration(),
                            tokenProperties.refreshExpiration());
                    return authResponseHelper.buildSuccessAuthResponse(user, "Not required", pair);
                }
            }
        } catch (Exception ignored) {
        }

        var profileOpt = googleTokenVerifier.verify(token);
        if (profileOpt.isPresent()) {
            var profile = profileOpt.get();
            var loginResult = oAuthLoginUseCase.login(profile, tokenProperties.expiration());
            var pair = refreshTokenUseCase.issueForEmail(profile.email(), tokenProperties.expiration(),
                    tokenProperties.refreshExpiration());
            var userOpt = userRepository.findByEmail(profile.email());
            UserEntity user = userOpt.isPresent()
                    ? userOpt.get()
                    : new UserEntity(
                            profile.givenName(),
                            profile.familyName(),
                            profile.email(),
                            UserRole.USER,
                            RandomPhoneGenerator.create(),
                            profile.picture());
            return authResponseHelper.buildSuccessAuthResponse(user,
                    loginResult.registered() ? "Registered" : "Not required", pair);
        }

        response.addHeader(HEADER_STATUS, "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.message("Unauthorized", HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserResponse>> me(@AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.message("Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        return ResponseEntity.ok(ApiResponse.ok(
                new AuthUserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRole(),
                        user.getPhone(),
                        user.getImage(),
                        user.getSubscribed()
                )
        ));
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
                .sameSite("Lax")
                .build();
        ResponseCookie clearedRefresh = ResponseCookie.from(securityProperties.jwt().refreshCookieName(), "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth")
                .maxAge(Duration.ZERO)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearedAccess.toString())
                .header(HttpHeaders.SET_COOKIE, clearedRefresh.toString())
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
                .sameSite("Lax")
                .build();
        ResponseCookie refresh = ResponseCookie.from(securityProperties.jwt().refreshCookieName(), pair.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth")
                .maxAge(Duration.ofSeconds(tokenProperties.refreshExpiration() * 60))
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, access.toString())
                .header(HttpHeaders.SET_COOKIE, refresh.toString())
                .body(ApiResponse.message("Tokens renovados", HttpStatus.OK));
    }
}
