package com.ubuntu.ubuntu_app.infra.errors;

import lombok.Getter;

@Getter
public class TokenException extends RuntimeException {    
    private String mensaje;

    public TokenException(String mensaje) {
        this.mensaje = mensaje;
    }
}
