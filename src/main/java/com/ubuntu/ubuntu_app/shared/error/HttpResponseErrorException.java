package com.ubuntu.ubuntu_app.shared.error;

public class HttpResponseErrorException extends RuntimeException {

    public HttpResponseErrorException(String message) {
        super(message);
    }
}
