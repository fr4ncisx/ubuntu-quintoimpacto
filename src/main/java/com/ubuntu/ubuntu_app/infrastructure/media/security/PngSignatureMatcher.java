package com.ubuntu.ubuntu_app.infrastructure.media.security;

import org.springframework.stereotype.Component;
import com.ubuntu.ubuntu_app.application.media.domain.MediaFormat;
import com.ubuntu.ubuntu_app.application.media.port.in.FileSignatureMatcher;
import java.util.Arrays;

@Component
public final class PngSignatureMatcher implements FileSignatureMatcher {

    private static final byte[] PNG_SIGNATURE = new byte[]{
        (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
    };

    @Override
    public MediaFormat supportedFormat() {
        return MediaFormat.PNG;
    }

    @Override
    public int requiredHeaderSize() {
        return PNG_SIGNATURE.length;
    }

    @Override
    public boolean matches(byte[] header) {
        if (header == null || header.length < PNG_SIGNATURE.length) {
            return false;
        }
        return Arrays.equals(Arrays.copyOf(header, PNG_SIGNATURE.length), PNG_SIGNATURE);
    }
}
