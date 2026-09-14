package com.ubuntu.ubuntu_app.shared.error;

public class JwtInvalidTokenException extends RuntimeException {

    public JwtInvalidTokenException(String message) {
        super(message);
    }
}
