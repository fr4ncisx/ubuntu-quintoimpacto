package com.ubuntu.ubuntu_app.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityJWTFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authRequest -> {
                    authRequest.requestMatchers("/user/update").hasRole("ADMIN");
                    authRequest.requestMatchers("/user/deactivate").hasRole("ADMIN");
                    authRequest.requestMatchers("/user/fetch").hasRole("ADMIN");
                    authRequest.requestMatchers("categories/new").hasRole("ADMIN");
                    authRequest.requestMatchers("/micro/new").hasRole("ADMIN");
                    authRequest.requestMatchers("/micro/edit").hasRole("ADMIN");
                    authRequest.requestMatchers("/user/register").hasRole("ADMIN");
                    authRequest.requestMatchers("/micro/edit").hasRole("ADMIN");
                    authRequest.requestMatchers("/micro/delete").hasRole("ADMIN");
                    authRequest.requestMatchers("/micro/hide").hasRole("ADMIN");
                    authRequest.requestMatchers("/categories/search").permitAll();
                    authRequest.requestMatchers("/micro/find").permitAll();
                    authRequest.requestMatchers("/micro/find/category").permitAll();
                    authRequest.requestMatchers("/paises").permitAll();
                    authRequest.requestMatchers("/provincias").permitAll();
                    authRequest.requestMatchers("/oauth2/login").permitAll();
                    authRequest.requestMatchers("/api/cloudinary/**").permitAll();
                    authRequest.requestMatchers("/swagger-ui.html", "/v3/api-docs/**",
                            "/swagger-ui/**").permitAll();
                    authRequest.anyRequest().authenticated();
                })
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    AuthenticationManager authManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
