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
                    // TODO: Usuarios
                    authRequest.requestMatchers("/user/update").hasRole("ADMIN");
                    authRequest.requestMatchers("/user/deactivate").hasRole("ADMIN");
                    authRequest.requestMatchers("/user/fetch").hasRole("ADMIN");
                    authRequest.requestMatchers("/user/register").hasRole("ADMIN");
                    // TODO: Categorias
                    authRequest.requestMatchers("categories/new").hasRole("ADMIN");
                    authRequest.requestMatchers("/categories/search").permitAll();
                    // TODO: Microemprendimientos
                    authRequest.requestMatchers("/micro/find").permitAll();
                    authRequest.requestMatchers("/micro/find/category").permitAll();
                    authRequest.requestMatchers("/micro/api/find-all").hasRole("ADMIN");
                    authRequest.requestMatchers("/micro/edit").hasRole("ADMIN");
                    authRequest.requestMatchers("/micro/delete").hasRole("ADMIN");
                    authRequest.requestMatchers("/micro/hide").hasRole("ADMIN");
                    authRequest.requestMatchers("/micro/new").hasRole("ADMIN");
                    authRequest.requestMatchers("/micro/edit").hasRole("ADMIN");
                    // TODO: Contact Request
                    authRequest.requestMatchers("/contact/new-request").permitAll();
                    authRequest.requestMatchers("/contact/reviewed").hasRole("ADMIN");
                    authRequest.requestMatchers("/contact/unreviewed").hasRole("ADMIN");
                    authRequest.requestMatchers("/contact/update").hasRole("ADMIN");
                    authRequest.requestMatchers("/contact/find").hasRole("ADMIN");
                    // TODO: Paises y provincias
                    authRequest.requestMatchers("/paises").permitAll();
                    authRequest.requestMatchers("/provincias").permitAll();
                    // TODO: Login
                    authRequest.requestMatchers("/oauth2/login").permitAll();
                    // TODO: Cloudinary API
                    authRequest.requestMatchers("/api/cloudinary/**").permitAll();
                    // TODO: Chatbot
                    authRequest.requestMatchers("/chatbot/faq/**").permitAll();
                    // TODO: Bugs
                    authRequest.requestMatchers("/bug/find").permitAll();
                    authRequest.requestMatchers("/bug/admin/**").hasRole("ADMIN");
                    // TODO: Swagger
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
