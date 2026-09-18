package com.ubuntu.ubuntu_app.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import com.ubuntu.ubuntu_app.shared.ratelimit.RateLimitFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain oauthLoginChain(HttpSecurity security,
            OAuthLoginSuccessHandler successHandler) throws Exception {
        return security
                .securityMatcher("/oauth2/authorization/**", "/login/oauth2/code/**")
                .cors(Customizer.withDefaults())
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authRequest -> authRequest.anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2.successHandler(successHandler))
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain apiChain(HttpSecurity security, SecurityJWTFilter jwtFilter,
            RateLimitFilter rateLimitFilter) throws Exception {
        CookieCsrfTokenRepository csrfRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfRepository.setCookieCustomizer(cookie -> cookie.sameSite("Lax").secure(true).path("/"));
        return security
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfRepository)
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers("/api/v1/**"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authRequest -> {
                    authRequest.requestMatchers("/swagger-ui.html", "/v3/api-docs/**",
                            "/swagger-ui/**").permitAll();
                    authRequest.requestMatchers("/actuator/health", "/actuator/health/**",
                            "/actuator/prometheus", "/actuator/info").permitAll();
                    authRequest.requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/refresh",
                            "/api/v1/auth/logout").permitAll();
                    authRequest.requestMatchers(HttpMethod.GET, "/api/v1/auth/me").hasAnyRole("ADMIN", "USER");
                    authRequest.requestMatchers(HttpMethod.GET, "/api/v1/countries", "/api/v1/provinces")
                            .permitAll();
                    authRequest.requestMatchers(HttpMethod.GET, "/api/v1/chatbot/**").permitAll();
                    authRequest.requestMatchers(HttpMethod.GET, "/api/v1/categories").permitAll();
                    authRequest.requestMatchers(HttpMethod.POST, "/api/v1/categories").hasRole("ADMIN");
                    authRequest.requestMatchers(HttpMethod.GET, "/api/v1/publications/statistics/**")
                            .hasRole("ADMIN");
                    authRequest.requestMatchers(HttpMethod.GET, "/api/v1/publications/**").permitAll();
                    authRequest.requestMatchers(HttpMethod.POST, "/api/v1/publications/*/views",
                            "/api/v1/contact-requests").permitAll();
                    authRequest.requestMatchers("/api/v1/publications/**").hasRole("ADMIN");
                    authRequest.requestMatchers(HttpMethod.GET, "/api/v1/microbusiness/search",
                            "/api/v1/microbusiness/near", "/api/v1/microbusiness", "/api/v1/microbusiness/all").permitAll();
                    authRequest.requestMatchers("/api/v1/microbusiness/**").hasRole("ADMIN");
                    authRequest.requestMatchers("/api/v1/contact-requests/**").hasRole("ADMIN");
                    authRequest.requestMatchers(HttpMethod.PUT, "/api/v1/users/*/deactivate").hasRole("ADMIN");
                    authRequest.requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN");
                    authRequest.requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN");
                    authRequest.requestMatchers("/api/v1/users/**").hasAnyRole("ADMIN", "USER");
                    authRequest.requestMatchers("/api/v1/cloudinary/**").hasRole("ADMIN");
                    authRequest.anyRequest().authenticated();
                })
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    AuthenticationManager authManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
