package com.ubuntu.ubuntu_app.infra.errors;

import lombok.Getter;

@Getter
public class EmptyFieldException extends RuntimeException {
    private String mensaje;

    public EmptyFieldException(String mensaje) {
        this.mensaje = mensaje;
    }
}
