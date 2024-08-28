package com.ubuntu.ubuntu_app.infra.errors;

import lombok.Getter;

@Getter
public class GeocodeErrorException extends RuntimeException{
    private String mensaje;

    public GeocodeErrorException(String mensaje) {
        this.mensaje = mensaje;
    }    
}
