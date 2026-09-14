package com.ubuntu.ubuntu_app.shared.error;

import lombok.Getter;

@Getter
public class IllegalRewriteException extends RuntimeException {    
    private String message;

    public IllegalRewriteException(String message) {
        this.message = message;
    }
}
