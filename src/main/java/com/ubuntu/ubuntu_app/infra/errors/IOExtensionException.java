package com.ubuntu.ubuntu_app.infra.errors;

import java.io.IOException;
import java.util.List;

import lombok.Getter;

@Getter
public class IOExtensionException extends IOException {
    private List<FileExtensionRecord> listOfErrors;

    public IOExtensionException(List<FileExtensionRecord> listOfErrors) {
        this.listOfErrors = listOfErrors;
    }

}
