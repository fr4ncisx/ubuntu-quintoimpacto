package com.ubuntu.ubuntu_app.application.user.api;

import com.ubuntu.ubuntu_app.application.user.UserRole;

public record AuthUserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        UserRole role,
        String phone,
        String image,
        Boolean subscribed) {
}
