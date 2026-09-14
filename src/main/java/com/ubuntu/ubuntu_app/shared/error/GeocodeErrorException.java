package com.ubuntu.ubuntu_app.shared.error;

import lombok.Getter;

@Getter
public class GeocodeErrorException extends RuntimeException{
    private String message;

    public GeocodeErrorException(String message) {
        this.message = message;
    }    
}
