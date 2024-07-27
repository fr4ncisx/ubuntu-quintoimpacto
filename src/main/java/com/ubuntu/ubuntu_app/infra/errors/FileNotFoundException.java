package com.ubuntu.ubuntu_app.infra.errors;

import java.io.IOException;

import lombok.Getter;

@Getter
public class FileNotFoundException extends IOException {
    private String mensaje;

    public FileNotFoundException(String mensaje) {
        this.mensaje = mensaje;
    }

}
