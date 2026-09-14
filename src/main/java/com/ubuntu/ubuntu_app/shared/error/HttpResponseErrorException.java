package com.ubuntu.ubuntu_app.shared.error;

import lombok.Getter;

@Getter
public class HttpResponseErrorException extends RuntimeException {

    private String message;

    public HttpResponseErrorException(String message) {
        this.message = message;
    }   

}
