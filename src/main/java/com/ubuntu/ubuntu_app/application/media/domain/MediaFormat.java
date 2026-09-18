package com.ubuntu.ubuntu_app.application.media.domain;

import java.util.Set;

public enum MediaFormat {
    JPEG("image/jpeg", Set.of("jpg", "jpeg"), 3 * 1024 * 1024),
    PNG("image/png", Set.of("png"), 3 * 1024 * 1024),
    WEBP("image/webp", Set.of("webp"), 3 * 1024 * 1024);

    private final String mimeType;
    private final Set<String> allowedExtensions;
    private final long maxSizeBytes;

    MediaFormat(String mimeType, Set<String> allowedExtensions, long maxSizeBytes) {
        this.mimeType = mimeType;
        this.allowedExtensions = allowedExtensions;
        this.maxSizeBytes = maxSizeBytes;
    }

    public String mimeType() {
        return mimeType;
    }

    public Set<String> allowedExtensions() {
        return allowedExtensions;
    }

    public boolean supportsExtension(String extension) {
        return extension != null && allowedExtensions.contains(extension.toLowerCase());
    }

    public long maxSizeBytes() {
        return maxSizeBytes;
    }
}
