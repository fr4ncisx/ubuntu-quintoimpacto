package com.ubuntu.ubuntu_app.shared.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;
import com.ubuntu.ubuntu_app.shared.config.SecurityProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class SecurityJWTFilter extends OncePerRequestFilter {

    private static final String PREFIX_TOKEN = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_STATUS = "Status";
    private static final String HEADER_LOGIN = "Login";
    private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
            "/api/v1/countries",
            "/api/v1/provinces",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/auth/logout",
            "/api/v1/categories",
            "/api/v1/publications",
            "/api/v1/publications/search",
            "/api/v1/microbusiness/search",
            "/api/v1/microbusiness/near",
            "/api/v1/microbusiness",
            "/api/v1/contact-requests",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/actuator/health");
    private static final List<String> PUBLIC_PATH_PREFIXES = Arrays.asList(
            "/swagger-ui/",
            "/v3/api-docs/",
            "/api/v1/publications/",
            "/api/v1/microbusiness/",
            "/api/v1/contact-requests/",
            "/api/v1/chatbot/",
            "/api/v1/categories/");

    static boolean isPublicUri(String uri) {
        if (uri == null) {
            return false;
        }
        return PUBLIC_ENDPOINTS.contains(uri) || PUBLIC_PATH_PREFIXES.stream().anyMatch(uri::startsWith);
    }

    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (isPublicUri(uri)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = resolveToken(request);
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"Error\": \"Authentication is required\"}");
            return;
        }
        if (token != null) {
            String email;
            try {
                email = jwtUtils.validateLocal(token);
            } catch (TokenExpiredException e) {
                log.warn("Expired JWT: ip={} uri={}", request.getRemoteAddr(), uri);
                response.setHeader(HEADER_LOGIN, "Token is expired");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"Error\": \"Authentication is required\"}");
                return;
            } catch (JWTVerificationException e) {
                log.warn("Invalid JWT: ip={} uri={}", request.getRemoteAddr(), uri);
                response.setHeader(HEADER_LOGIN, "Invalid token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"Error\": \"Authentication is required\"}");
                return;
            }
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var user = userRepository.findByEmail(email);
                if (user.isPresent()) {
                    UserEntity userEntity = user.get();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userEntity, null,
                                    userEntity.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    response.setHeader(HEADER_STATUS, "Authorized");
                    filterChain.doFilter(request, response);
                    return;
                }
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader(HEADER_STATUS, "Invalid token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(PREFIX_TOKEN)) {
            return authorizationHeader.substring(PREFIX_TOKEN.length());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (securityProperties.jwt().cookieName().equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
