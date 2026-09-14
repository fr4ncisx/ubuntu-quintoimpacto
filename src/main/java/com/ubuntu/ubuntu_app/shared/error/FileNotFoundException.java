package com.ubuntu.ubuntu_app.shared.error;

import java.io.IOException;

public class FileNotFoundException extends IOException {

    public FileNotFoundException(String message) {
        super(message);
    }
}
