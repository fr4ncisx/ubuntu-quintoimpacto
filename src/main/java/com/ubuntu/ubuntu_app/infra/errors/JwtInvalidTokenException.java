package com.ubuntu.ubuntu_app.infra.errors;

import lombok.Getter;

@Getter
public class JwtInvalidTokenException extends RuntimeException {
    private String mensaje;

    public JwtInvalidTokenException(String mensaje){
        this.mensaje = mensaje;
    }

}
