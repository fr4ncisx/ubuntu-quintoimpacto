package com.ubuntu.ubuntu_app.shared.error;

import java.io.IOException;

import lombok.Getter;

@Getter
public class CloudinaryFileNotFoundException extends IOException {
    private String message;

    public CloudinaryFileNotFoundException(String message) {
        this.message = message;
    }

}
