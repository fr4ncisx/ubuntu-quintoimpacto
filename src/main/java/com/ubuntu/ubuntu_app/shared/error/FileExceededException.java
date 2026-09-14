package com.ubuntu.ubuntu_app.shared.error;

import java.io.IOException;

public class FileExceededException extends IOException {

    public FileExceededException(String message) {
        super(message);
    }
}
