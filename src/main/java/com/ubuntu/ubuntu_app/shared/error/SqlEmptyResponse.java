package com.ubuntu.ubuntu_app.shared.error;

public class SqlEmptyResponse extends RuntimeException {

    public SqlEmptyResponse(String message) {
        super(message);
    }
}
