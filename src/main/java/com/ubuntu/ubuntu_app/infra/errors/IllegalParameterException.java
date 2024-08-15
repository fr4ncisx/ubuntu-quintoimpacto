package com.ubuntu.ubuntu_app.infra.errors;

import lombok.Getter;

@Getter
public class IllegalParameterException extends RuntimeException {    
    private String mensaje;

    public IllegalParameterException(String mensaje) {
        this.mensaje = mensaje;
    }
}
