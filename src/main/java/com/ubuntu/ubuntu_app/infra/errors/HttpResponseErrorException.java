package com.ubuntu.ubuntu_app.infra.errors;

import lombok.Getter;

@Getter
public class HttpResponseErrorException extends RuntimeException {

    private String mensaje;

    public HttpResponseErrorException(String mensaje) {
        this.mensaje = mensaje;
    }   

}
