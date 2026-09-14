package com.ubuntu.ubuntu_app.shared.error;

import java.io.IOException;
import java.util.List;

public class IOExtensionException extends IOException {

    private final List<FileExtensionRecord> listOfErrors;

    public IOExtensionException(List<FileExtensionRecord> listOfErrors) {
        this.listOfErrors = listOfErrors;
    }

    public List<FileExtensionRecord> getListOfErrors() {
        return listOfErrors;
    }
}
