package com.ubuntu.ubuntu_app.infra.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.ubuntu.ubuntu_app.model.entities.UserEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityJWTFilter extends OncePerRequestFilter {

    private static final String PREFIX_TOKEN = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_STATUS = "Status";
    private static final String HEADER_LOGIN = "Login";
    private static final String HEADER_REGISTRATION = "Registration";
    private final List<String> publicEndpoints = Arrays.asList(
            "/categories/search",
            "/contact/new-request",
            "/publications/find",
            "/publications/click",
            "/publications/search",
            "/publications/find-all",
            "/micro/find",
            "/micro/find/category",
            "/bug/find",
            "/bug/fixed",
            "/paises",
            "/provincias",
            "/api/cloudinary",
            "/oauth2/login",
            "/chatbot",
            "/swagger-ui.html",
            "/swagger-ui/",
            "/v3/api-docs");

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;
        String uri = request.getRequestURI();
        boolean isPublicEndpoint = publicEndpoints.stream().anyMatch(uri::startsWith);
        if (authorizationHeader == null && !isPublicEndpoint) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            // response.sendRedirect("http://localhost:5173/login");
            response.getWriter().write("{\"Error\": \"Authentication is required\"}");
            return;
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.replace("Bearer ", "");
            Payload payload = jwtUtils.extractGooglePayload(token);
            if (payload != null) {
                email = payload.getEmail();
                UserEntity userObtained = jwtUtils.userFinder(email);
                if (userObtained != null) {
                    String newToken = jwtUtils.generateToken(userObtained, payload);
                    response.setHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + newToken);
                    response.setHeader(HEADER_REGISTRATION, "Not required");
                } else {
                    String registerToken = jwtUtils.createLocalAccount(payload);
                    if (registerToken != null) {
                        response.setHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + registerToken);
                        response.setHeader(HEADER_REGISTRATION, "Registered");
                    }
                }
            } else {
                try {
                    email = jwtUtils.validateTokenLocal(token);
                } catch (TokenExpiredException e) {
                    response.setHeader(HEADER_LOGIN, "Token is expired");
                    response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    response.getWriter().write("{\"Error\": \"Authentication is required\"}");
                    return;
                } catch (SignatureVerificationException ex) {
                    response.setHeader(HEADER_LOGIN, "Invalid signature");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } catch (AlgorithmMismatchException x) {
                    response.setHeader(HEADER_LOGIN, "Invalid algorithm");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            }
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity user = jwtUtils.userFinder(email);
            if (jwtUtils.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                response.setHeader(HEADER_STATUS, "Authorized");
                filterChain.doFilter(request, response);
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setHeader(HEADER_STATUS, "Failed token valid but not in database");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
