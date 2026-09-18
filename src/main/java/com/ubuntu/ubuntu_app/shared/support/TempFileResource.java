package com.ubuntu.ubuntu_app.shared.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class TempFileResource implements AutoCloseable {

    private final Path path;

    private TempFileResource(Path path) {
        this.path = path;
    }

    public static TempFileResource fromStream(InputStream in, String prefix, String suffix) throws IOException {
        Path tempPath = Files.createTempFile(prefix, suffix);
        Files.copy(in, tempPath, StandardCopyOption.REPLACE_EXISTING);
        return new TempFileResource(tempPath);
    }

    public Path path() {
        return path;
    }

    public File toFile() {
        return path.toFile();
    }

    @Override
    public void close() {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}
