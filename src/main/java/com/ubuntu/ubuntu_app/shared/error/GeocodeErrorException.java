package com.ubuntu.ubuntu_app.shared.error;

public class GeocodeErrorException extends RuntimeException {

    public GeocodeErrorException(String message) {
        super(message);
    }
}
