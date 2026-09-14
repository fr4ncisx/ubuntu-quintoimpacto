package com.ubuntu.ubuntu_app.shared.error;

import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException {    
    private String message;

    public EmailNotFoundException(String message) {
        this.message = message;
    }
}
