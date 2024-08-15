package com.ubuntu.ubuntu_app.infra.errors;

import lombok.Getter;

@Getter
public class SqlEmptyResponse extends RuntimeException{
    private String mensaje;

    public SqlEmptyResponse(String mensaje) {
        this.mensaje = mensaje;
    }  
}
