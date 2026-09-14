package com.ubuntu.ubuntu_app.shared.error;

import lombok.Getter;

@Getter
public class SqlEmptyResponse extends RuntimeException{
    private String message;

    public SqlEmptyResponse(String message) {
        this.message = message;
    }  
}
