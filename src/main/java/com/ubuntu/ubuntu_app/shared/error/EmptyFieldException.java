package com.ubuntu.ubuntu_app.shared.error;

import lombok.Getter;

@Getter
public class EmptyFieldException extends RuntimeException {
    private String message;

    public EmptyFieldException(String message) {
        this.message = message;
    }
}
