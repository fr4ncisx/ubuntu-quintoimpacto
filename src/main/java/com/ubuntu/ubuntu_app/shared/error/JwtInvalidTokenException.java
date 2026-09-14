package com.ubuntu.ubuntu_app.shared.error;

import lombok.Getter;

@Getter
public class JwtInvalidTokenException extends RuntimeException {
    private String message;

    public JwtInvalidTokenException(String message){
        this.message = message;
    }

}
