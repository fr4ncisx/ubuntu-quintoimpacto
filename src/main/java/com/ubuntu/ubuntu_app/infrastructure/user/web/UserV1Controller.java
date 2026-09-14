package com.ubuntu.ubuntu_app.infrastructure.user.web;

import java.util.List;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.user.port.in.UserUseCase;
import com.ubuntu.ubuntu_app.application.user.api.RegisterUserRequest;
import com.ubuntu.ubuntu_app.application.user.api.UserResponse;
import com.ubuntu.ubuntu_app.application.user.api.UpdateUserRequest;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;
import com.ubuntu.ubuntu_app.shared.api.Pages;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserV1Controller {

    private final UserUseCase userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> createUser(
            @RequestBody @Valid RegisterUserRequest request) {
        return new ResponseEntity<>(ApiResponse.created(userService.register(request)),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(
                userService.findAll(Pages.of(page, size, Sort.by("id")))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> update(@PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(userService.updateById(id, request)));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Map<String, String>>> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.deactivate(id)));
    }
}
