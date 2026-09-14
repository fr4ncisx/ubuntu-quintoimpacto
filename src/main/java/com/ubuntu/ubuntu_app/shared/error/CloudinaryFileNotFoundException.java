package com.ubuntu.ubuntu_app.shared.error;

import java.io.IOException;

public class CloudinaryFileNotFoundException extends IOException {

    public CloudinaryFileNotFoundException(String message) {
        super(message);
    }
}
