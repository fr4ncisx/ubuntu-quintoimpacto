package com.ubuntu.ubuntu_app.infra.errors;

import java.io.IOException;

import lombok.Getter;

@Getter
public class CloudinaryFileNotFoundException extends IOException {
    private String mensaje;

    public CloudinaryFileNotFoundException(String mensaje) {
        this.mensaje = mensaje;
    }

}
