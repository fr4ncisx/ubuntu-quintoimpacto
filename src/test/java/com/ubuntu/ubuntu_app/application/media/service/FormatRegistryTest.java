package com.ubuntu.ubuntu_app.application.media.service;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ubuntu.ubuntu_app.application.media.domain.MediaFormat;
import com.ubuntu.ubuntu_app.infrastructure.media.security.JpegSignatureMatcher;
import com.ubuntu.ubuntu_app.infrastructure.media.security.PngSignatureMatcher;
import com.ubuntu.ubuntu_app.infrastructure.media.security.WebpSignatureMatcher;
import com.ubuntu.ubuntu_app.shared.error.SecurityValidationException;

import static org.junit.jupiter.api.Assertions.*;

class FormatRegistryTest {

    private FormatRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new FormatRegistry(List.of(
            new JpegSignatureMatcher(),
            new PngSignatureMatcher(),
            new WebpSignatureMatcher()
        ));
    }

    @Test
    void resolvesFormatFromAllowedExtensionsCaseInsensitive() {
        assertEquals(MediaFormat.JPEG, registry.resolveFormat("jpg"));
        assertEquals(MediaFormat.JPEG, registry.resolveFormat("JPEG"));
        assertEquals(MediaFormat.PNG, registry.resolveFormat("png"));
        assertEquals(MediaFormat.WEBP, registry.resolveFormat("webp"));
    }

    @Test
    void rejectsDisallowedOrNullExtensions() {
        assertThrows(SecurityValidationException.class, () -> registry.resolveFormat("exe"));
        assertThrows(SecurityValidationException.class, () -> registry.resolveFormat("php"));
        assertThrows(SecurityValidationException.class, () -> registry.resolveFormat("svg"));
        assertThrows(SecurityValidationException.class, () -> registry.resolveFormat(null));
    }

    @Test
    void verifiesValidJpegSignature() {
        byte[] validJpegHeader = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertDoesNotThrow(() -> registry.verifySignature(MediaFormat.JPEG, validJpegHeader));
    }

    @Test
    void verifiesValidPngSignature() {
        byte[] validPngHeader = new byte[]{
            (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0, 0, 0, 0
        };
        assertDoesNotThrow(() -> registry.verifySignature(MediaFormat.PNG, validPngHeader));
    }

    @Test
    void verifiesValidWebpSignature() {
        byte[] validWebpHeader = new byte[]{
            0x52, 0x49, 0x46, 0x46, 0, 0, 0, 0, 0x57, 0x45, 0x42, 0x50
        };
        assertDoesNotThrow(() -> registry.verifySignature(MediaFormat.WEBP, validWebpHeader));
    }

    @Test
    void rejectsMimeSpoofedBinaryHeader() {
        byte[] fakeJpegHeader = new byte[]{0x4D, 0x5A, (byte) 0x90, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertThrows(SecurityValidationException.class, () -> registry.verifySignature(MediaFormat.JPEG, fakeJpegHeader));
    }
}
