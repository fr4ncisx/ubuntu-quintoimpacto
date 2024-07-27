package com.ubuntu.ubuntu_app.infra.errors;

import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException {    
    private String mensaje;

    public EmailNotFoundException(String mensaje) {
        this.mensaje = mensaje;
    }
}
