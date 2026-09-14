package com.ubuntu.ubuntu_app.shared.error;

import java.io.IOException;

import lombok.Getter;

@Getter
public class FileExceededException extends IOException {
    private String message;

    public FileExceededException(String message) {
        this.message = message;
    }
}
