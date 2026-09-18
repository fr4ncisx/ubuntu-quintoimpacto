package com.ubuntu.ubuntu_app.infrastructure.media.security;

import org.springframework.stereotype.Component;
import com.ubuntu.ubuntu_app.application.media.domain.MediaFormat;
import com.ubuntu.ubuntu_app.application.media.port.in.FileSignatureMatcher;
import java.nio.charset.StandardCharsets;

@Component
public final class WebpSignatureMatcher implements FileSignatureMatcher {

    private static final byte[] RIFF_HEADER = "RIFF".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] WEBP_HEADER = "WEBP".getBytes(StandardCharsets.US_ASCII);

    @Override
    public MediaFormat supportedFormat() {
        return MediaFormat.WEBP;
    }

    @Override
    public int requiredHeaderSize() {
        return 12;
    }

    @Override
    public boolean matches(byte[] header) {
        if (header == null || header.length < 12) {
            return false;
        }
        boolean isRiff = header[0] == RIFF_HEADER[0] && header[1] == RIFF_HEADER[1]
                && header[2] == RIFF_HEADER[2] && header[3] == RIFF_HEADER[3];
        boolean isWebp = header[8] == WEBP_HEADER[0] && header[9] == WEBP_HEADER[1]
                && header[10] == WEBP_HEADER[2] && header[11] == WEBP_HEADER[3];
        return isRiff && isWebp;
    }
}
