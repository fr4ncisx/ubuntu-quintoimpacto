package com.ubuntu.ubuntu_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Login")
@RestController
public class AuthController {

    @PostMapping("/oauth2/login")
    public ResponseEntity<?> receiveTokenByHeader() {
        return ResponseEntity.badRequest().build();
    }
}
