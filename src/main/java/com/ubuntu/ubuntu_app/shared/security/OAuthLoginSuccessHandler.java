package com.ubuntu.ubuntu_app.shared.security;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.auth.port.in.RefreshTokenUseCase;
import com.ubuntu.ubuntu_app.application.user.api.GoogleOidcProfile;
import com.ubuntu.ubuntu_app.application.user.port.in.OAuthLoginUseCase;
import com.ubuntu.ubuntu_app.shared.config.SecurityProperties;
import com.ubuntu.ubuntu_app.shared.config.TokenProperties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthLoginUseCase oAuthLoginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final SecurityProperties securityProperties;
    private final TokenProperties tokenProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        if (!(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            response.sendRedirect(securityProperties.google().postLoginRedirect()
                    + "?error=unsupported_principal");
            return;
        }
        if (!Boolean.TRUE.equals(oidcUser.getEmailVerified())) {
            log.warn("Login OAuth rechazado: email no verificado");
            response.sendRedirect(securityProperties.google().postLoginRedirect()
                    + "?error=email_not_verified");
            return;
        }
        var profile = new GoogleOidcProfile(
                oidcUser.getEmail(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                oidcUser.getPicture(),
                oidcUser.getEmailVerified());
        var login = oAuthLoginUseCase.login(profile, tokenProperties.expiration());
        var pair = refreshTokenUseCase.issueForEmail(profile.email(), tokenProperties.expiration(),
                tokenProperties.refreshExpiration());
        response.addHeader("Set-Cookie", accessCookie(pair.accessToken()).toString());
        response.addHeader("Set-Cookie", refreshCookie(pair.refreshToken()).toString());
        response.sendRedirect(securityProperties.google().postLoginRedirect());
    }

    private ResponseCookie accessCookie(String token) {
        return ResponseCookie.from(securityProperties.jwt().cookieName(), token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofSeconds((long) tokenProperties.expiration() * 60))
                .sameSite("None")
                .build();
    }

    private ResponseCookie refreshCookie(String token) {
        return ResponseCookie.from(securityProperties.jwt().refreshCookieName(), token)
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth")
                .maxAge(Duration.ofSeconds(tokenProperties.refreshExpiration() * 60))
                .sameSite("None")
                .build();
    }
}
