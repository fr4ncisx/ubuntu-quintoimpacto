package com.ubuntu.ubuntu_app.infrastructure.media.security;

import org.springframework.stereotype.Component;
import com.ubuntu.ubuntu_app.application.media.domain.MediaFormat;
import com.ubuntu.ubuntu_app.application.media.port.in.FileSignatureMatcher;

@Component
public final class JpegSignatureMatcher implements FileSignatureMatcher {

    private static final byte[] SOI_MARKER = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};

    @Override
    public MediaFormat supportedFormat() {
        return MediaFormat.JPEG;
    }

    @Override
    public int requiredHeaderSize() {
        return SOI_MARKER.length;
    }

    @Override
    public boolean matches(byte[] header) {
        if (header == null || header.length < SOI_MARKER.length) {
            return false;
        }
        return header[0] == SOI_MARKER[0] && header[1] == SOI_MARKER[1] && header[2] == SOI_MARKER[2];
    }
}
