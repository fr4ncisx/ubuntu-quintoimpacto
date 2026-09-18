package com.ubuntu.ubuntu_app.application.media.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.ubuntu.ubuntu_app.application.media.domain.MediaFormat;
import com.ubuntu.ubuntu_app.application.media.port.in.FileSignatureMatcher;
import com.ubuntu.ubuntu_app.shared.error.SecurityValidationException;

@Component
public class FormatRegistry {

    private static final Map<String, MediaFormat> EXTENSION_MAP = Map.of(
        "jpg", MediaFormat.JPEG,
        "jpeg", MediaFormat.JPEG,
        "png", MediaFormat.PNG,
        "webp", MediaFormat.WEBP
    );

    private final EnumMap<MediaFormat, FileSignatureMatcher> strategyMap;
    private final int maxRequiredHeader;

    public FormatRegistry(List<FileSignatureMatcher> matchers) {
        this.strategyMap = new EnumMap<>(MediaFormat.class);
        int maxHeader = 0;
        for (FileSignatureMatcher matcher : matchers) {
            this.strategyMap.put(matcher.supportedFormat(), matcher);
            maxHeader = Math.max(maxHeader, matcher.requiredHeaderSize());
        }
        this.maxRequiredHeader = Math.max(maxHeader, 12);
    }

    public MediaFormat resolveFormat(String extension) {
        if (extension == null) {
            throw new SecurityValidationException("Extensión nula no permitida");
        }
        MediaFormat format = EXTENSION_MAP.get(extension.toLowerCase());
        if (format == null) {
            throw new SecurityValidationException("Extensión no autorizada: " + extension);
        }
        return format;
    }

    public void verifySignature(MediaFormat format, byte[] header) {
        FileSignatureMatcher matcher = strategyMap.get(format);
        if (matcher == null || !matcher.matches(header)) {
            throw new SecurityValidationException("Firma binaria inválida para formato " + format);
        }
    }

    public int maxRequiredHeader() {
        return maxRequiredHeader;
    }
}
