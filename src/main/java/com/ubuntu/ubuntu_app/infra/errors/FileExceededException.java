package com.ubuntu.ubuntu_app.infra.errors;

import java.io.IOException;

import lombok.Getter;

@Getter
public class FileExceededException extends IOException {
    private String mensaje;

    public FileExceededException(String mensaje) {
        this.mensaje = mensaje;
    }
}
