package com.ubuntu.ubuntu_app.application.user.api;

public record GoogleUserProfile(
        String email,
        String firstName,
        String lastName,
        String profileImage) {
}
