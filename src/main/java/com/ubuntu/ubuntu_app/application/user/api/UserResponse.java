package com.ubuntu.ubuntu_app.application.user.api;

import com.ubuntu.ubuntu_app.application.user.UserRole;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        boolean active,
        UserRole role,
        String phone) {
}
