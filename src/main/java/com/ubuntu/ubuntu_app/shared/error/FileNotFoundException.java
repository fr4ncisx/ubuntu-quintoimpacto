package com.ubuntu.ubuntu_app.shared.error;

import java.io.IOException;

import lombok.Getter;

@Getter
public class FileNotFoundException extends IOException {
    private String message;

    public FileNotFoundException(String message) {
        this.message = message;
    }

}
