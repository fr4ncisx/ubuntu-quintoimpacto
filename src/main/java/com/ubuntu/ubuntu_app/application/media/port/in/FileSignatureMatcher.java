package com.ubuntu.ubuntu_app.application.media.port.in;

import com.ubuntu.ubuntu_app.application.media.domain.MediaFormat;

public interface FileSignatureMatcher {
    MediaFormat supportedFormat();
    int requiredHeaderSize();
    boolean matches(byte[] header);
}
