package com.ubuntu.ubuntu_app.infra.errors;

import lombok.Getter;

@Getter
public class IllegalRewriteException extends RuntimeException {    
    private String mensaje;

    public IllegalRewriteException(String mensaje) {
        this.mensaje = mensaje;
    }
}
