package com.ubuntu.ubuntu_app.shared.error;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(String message) {
        super(message);
    }
}
