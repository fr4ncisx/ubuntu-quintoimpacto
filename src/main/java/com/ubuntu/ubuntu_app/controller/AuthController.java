package com.ubuntu.ubuntu_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Login")
@RestController
public class AuthController {

    private static final String HEADER_STATUS = "Status";

    @PostMapping("/oauth2/login")
    public ResponseEntity<?> receiveTokenByHeader(
            @RequestHeader(name = "Authorization", required = false) String header, HttpServletResponse response) {
        if (header == null) {
            response.addHeader(HEADER_STATUS, "Missing token");
            return ResponseEntity.badRequest().build();
        }
        return null;
    }
}
