package com.ubuntu.ubuntu_app.infra.errors;

import lombok.Getter;

@Getter
public class SQLemptyDataException extends RuntimeException{
    private String mensaje;

    public SQLemptyDataException(String mensaje) {
        this.mensaje = mensaje;
    }  
}
