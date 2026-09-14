package com.ubuntu.ubuntu_app.application.user.api;

public record GoogleOidcProfile(
        String email,
        String givenName,
        String familyName,
        String picture,
        Boolean emailVerified) {
}
