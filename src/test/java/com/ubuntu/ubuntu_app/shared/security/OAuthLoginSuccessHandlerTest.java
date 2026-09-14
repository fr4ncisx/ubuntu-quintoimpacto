package com.ubuntu.ubuntu_app.shared.security;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.ubuntu.ubuntu_app.application.auth.port.in.RefreshTokenUseCase;
import com.ubuntu.ubuntu_app.application.user.port.in.OAuthLoginResult;
import com.ubuntu.ubuntu_app.application.user.port.in.OAuthLoginUseCase;
import com.ubuntu.ubuntu_app.shared.config.SecurityProperties;
import com.ubuntu.ubuntu_app.shared.config.TokenProperties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

class OAuthLoginSuccessHandlerTest {

    private OAuthLoginSuccessHandler handler(String redirect, OAuthLoginUseCase useCase,
            RefreshTokenUseCase refreshUseCase) {
        return new OAuthLoginSuccessHandler(useCase, refreshUseCase,
                new SecurityProperties(
                        new SecurityProperties.Google(redirect),
                        new SecurityProperties.Jwt("ubuntu_jwt", "ubuntu_refresh")),
                new TokenProperties(15, 10080L));
    }

    private OAuth2AuthenticationToken authentication(boolean emailVerified) {
        OidcIdToken idToken = new OidcIdToken("id-token", Instant.now().minusSeconds(10),
                Instant.now().plusSeconds(300),
                Map.of("sub", "123", "email", "user@mail.com", "given_name", "Nombre",
                        "family_name", "Apellido", "picture", "http://img/x.jpg",
                        "email_verified", emailVerified));
        OidcUser oidcUser = new DefaultOidcUser(List.of(), idToken);
        return new OAuth2AuthenticationToken(oidcUser, List.of(), "google");
    }

    @Test
    void successSetsCookieAndRedirects() throws Exception {
        OAuthLoginUseCase useCase = Mockito.mock(OAuthLoginUseCase.class);
        RefreshTokenUseCase refreshUseCase = Mockito.mock(RefreshTokenUseCase.class);
        Mockito.when(useCase.login(any(), anyInt()))
                .thenReturn(new OAuthLoginResult("local-jwt", true));
        Mockito.when(refreshUseCase.issueForEmail(anyString(), anyInt(), anyLong()))
                .thenReturn(new RefreshTokenUseCase.TokenPair("local-jwt", "raw-refresh"));
        var handler = handler("http://front/home", useCase, refreshUseCase);
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.onAuthenticationSuccess(new MockHttpServletRequest(), response,
                authentication(true));

        assertEquals("http://front/home", response.getRedirectedUrl());
        var setCookies = response.getHeaders("Set-Cookie");
        assertEquals(2, setCookies.size());
        String access = setCookies.get(0);
        String refresh = setCookies.get(1);
        assertTrue(access.contains("ubuntu_jwt=local-jwt"));
        assertTrue(access.contains("HttpOnly"));
        assertTrue(access.contains("SameSite=None"));
        assertTrue(access.contains("Secure"));
        assertTrue(refresh.contains("ubuntu_refresh=raw-refresh"));
        assertTrue(refresh.contains("Path=/api/v1/auth"));
    }

    @Test
    void unverifiedEmailRedirectsWithError() throws Exception {
        OAuthLoginUseCase useCase = Mockito.mock(OAuthLoginUseCase.class);
        RefreshTokenUseCase refreshUseCase = Mockito.mock(RefreshTokenUseCase.class);
        var handler = handler("http://front/home", useCase, refreshUseCase);
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.onAuthenticationSuccess(new MockHttpServletRequest(), response,
                authentication(false));

        assertEquals("http://front/home?error=email_not_verified", response.getRedirectedUrl());
        assertNull(response.getHeader("Set-Cookie"));
        Mockito.verify(useCase, Mockito.never()).login(any(), anyInt());
    }
}
