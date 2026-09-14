package com.ubuntu.ubuntu_app.shared.error;

import lombok.Getter;

@Getter
public class IllegalParameterException extends RuntimeException {    
    private String message;

    public IllegalParameterException(String message) {
        this.message = message;
    }
}
