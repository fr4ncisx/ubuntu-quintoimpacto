package com.ubuntu.ubuntu_app.application.media.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.ubuntu.ubuntu_app.application.media.api.CloudinaryResult;
import com.ubuntu.ubuntu_app.infrastructure.media.security.JpegSignatureMatcher;
import com.ubuntu.ubuntu_app.infrastructure.media.security.PngSignatureMatcher;
import com.ubuntu.ubuntu_app.infrastructure.media.security.WebpSignatureMatcher;
import com.ubuntu.ubuntu_app.shared.error.CloudinaryFileNotFoundException;
import com.ubuntu.ubuntu_app.shared.error.FileExceededException;
import com.ubuntu.ubuntu_app.shared.error.FileNotFoundException;
import com.ubuntu.ubuntu_app.shared.error.SecurityValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    private static final byte[] VALID_JPEG = new byte[]{
        (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4
    };
    private static final byte[] VALID_PNG = new byte[]{
        (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0, 0, 0, 0, 1, 2, 3, 4
    };
    private static final byte[] VALID_WEBP = new byte[]{
        0x52, 0x49, 0x46, 0x46, 0, 0, 0, 0, 0x57, 0x45, 0x42, 0x50, 1, 2, 3, 4
    };

    @Mock
    private CloudinaryService cloudinaryService;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        FormatRegistry formatRegistry = new FormatRegistry(List.of(
            new JpegSignatureMatcher(),
            new PngSignatureMatcher(),
            new WebpSignatureMatcher()
        ));
        imageService = new ImageService(formatRegistry, cloudinaryService);
    }

    @Test
    void uploadRejectsNullOrEmptyFilesArray() {
        assertThrows(FileNotFoundException.class, () -> imageService.upload(null));
        assertThrows(FileNotFoundException.class, () -> imageService.upload(new MultipartFile[0]));
    }

    @Test
    void uploadRejectsMoreThanThreeFiles() {
        MultipartFile[] fourFiles = new MultipartFile[]{
            new MockMultipartFile("files", "1.jpg", "image/jpeg", VALID_JPEG),
            new MockMultipartFile("files", "2.jpg", "image/jpeg", VALID_JPEG),
            new MockMultipartFile("files", "3.jpg", "image/jpeg", VALID_JPEG),
            new MockMultipartFile("files", "4.jpg", "image/jpeg", VALID_JPEG)
        };
        assertThrows(FileExceededException.class, () -> imageService.upload(fourFiles));
    }

    @Test
    void uploadRejectsEmptyFile() {
        MultipartFile emptyFile = new MockMultipartFile("files", "empty.jpg", "image/jpeg", new byte[0]);
        assertThrows(SecurityValidationException.class, () -> imageService.upload(new MultipartFile[]{emptyFile}));
    }

    @Test
    void uploadRejectsFileExceedingThreeMegaBytes() {
        byte[] oversized = new byte[3 * 1024 * 1024 + 1];
        System.arraycopy(VALID_JPEG, 0, oversized, 0, VALID_JPEG.length);
        MultipartFile heavyFile = new MockMultipartFile("files", "heavy.jpg", "image/jpeg", oversized);
        assertThrows(FileExceededException.class, () -> imageService.upload(new MultipartFile[]{heavyFile}));
    }

    @Test
    void uploadRejectsInvalidOrMissingExtension() {
        MultipartFile noExt = new MockMultipartFile("files", "testfile", "image/jpeg", VALID_JPEG);
        assertThrows(SecurityValidationException.class, () -> imageService.upload(new MultipartFile[]{noExt}));

        MultipartFile illegalExt = new MockMultipartFile("files", "malicious.sh", "application/x-sh", VALID_JPEG);
        assertThrows(SecurityValidationException.class, () -> imageService.upload(new MultipartFile[]{illegalExt}));
    }

    @Test
    void uploadRejectsTruncatedFile() {
        MultipartFile truncated = new MockMultipartFile("files", "short.jpg", "image/jpeg", new byte[]{(byte) 0xFF, (byte) 0xD8});
        assertThrows(SecurityValidationException.class, () -> imageService.upload(new MultipartFile[]{truncated}));
    }

    @Test
    void uploadRejectsSpoofedMagicBytes() {
        byte[] spoofed = new byte[]{0x4D, 0x5A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        MultipartFile fakeImage = new MockMultipartFile("files", "trojan.png", "image/png", spoofed);
        assertThrows(SecurityValidationException.class, () -> imageService.upload(new MultipartFile[]{fakeImage}));
    }

    @Test
    void uploadSingleFileReturnsSingleKeyContract() throws IOException {
        MultipartFile singleFile = new MockMultipartFile("files", "photo.jpg", "image/jpeg", VALID_JPEG);

        doAnswer(invocation -> {
            Map<String, String> map = invocation.getArgument(1);
            map.put("Imagen", "https://res.cloudinary.com/demo/image/upload/photo.jpg");
            return null;
        }).when(cloudinaryService).uploadSingle(any(File.class), anyMap());

        Map<String, String> result = imageService.upload(new MultipartFile[]{singleFile});

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("https://res.cloudinary.com/demo/image/upload/photo.jpg", result.get("Imagen"));
    }

    @Test
    void uploadMultipleFilesReturnsIndexedKeyContract() throws IOException {
        MultipartFile file1 = new MockMultipartFile("files", "a.jpg", "image/jpeg", VALID_JPEG);
        MultipartFile file2 = new MockMultipartFile("files", "b.png", "image/png", VALID_PNG);
        MultipartFile file3 = new MockMultipartFile("files", "c.webp", "image/webp", VALID_WEBP);

        doAnswer(invocation -> {
            Map<String, String> map = invocation.getArgument(1);
            map.put("Imagen", "https://res.cloudinary.com/demo/image/upload/img.jpg");
            return null;
        }).when(cloudinaryService).uploadSingle(any(File.class), anyMap());

        Map<String, String> result = imageService.upload(new MultipartFile[]{file1, file2, file3});

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("https://res.cloudinary.com/demo/image/upload/img.jpg", result.get("Imagen 1"));
        assertEquals("https://res.cloudinary.com/demo/image/upload/img.jpg", result.get("Imagen 2"));
        assertEquals("https://res.cloudinary.com/demo/image/upload/img.jpg", result.get("Imagen 3"));
    }

    @Test
    void deleteThrowsCloudinaryFileNotFoundExceptionWhenNotFound() throws IOException {
        when(cloudinaryService.delete("missing-id")).thenReturn(new CloudinaryResult("not found"));
        assertThrows(CloudinaryFileNotFoundException.class, () -> imageService.delete("missing-id"));
    }

    @Test
    void deleteThrowsIOExceptionWhenNotOk() throws IOException {
        when(cloudinaryService.delete("err-id")).thenReturn(new CloudinaryResult("error"));
        assertThrows(IOException.class, () -> imageService.delete("err-id"));
    }

    @Test
    void deleteReturnsCloudinaryResultWhenOk() throws IOException {
        when(cloudinaryService.delete("ok-id")).thenReturn(new CloudinaryResult("ok"));
        CloudinaryResult result = imageService.delete("ok-id");
        assertEquals("ok", result.result());
    }

    @Test
    void replaceRejectsNullFile() {
        assertThrows(FileNotFoundException.class, () -> imageService.replace(null, "some-id"));
    }

    @Test
    void replaceDeletesOldAndUploadsNewFile() throws IOException {
        MultipartFile replacement = new MockMultipartFile("image", "new.webp", "image/webp", VALID_WEBP);

        when(cloudinaryService.delete("existing-id")).thenReturn(new CloudinaryResult("ok"));
        doAnswer(invocation -> {
            Map<String, String> map = invocation.getArgument(1);
            map.put("Imagen", "https://res.cloudinary.com/demo/image/upload/new.webp");
            return null;
        }).when(cloudinaryService).uploadSingle(any(File.class), anyMap());

        Map<String, String> result = imageService.replace(replacement, "existing-id");

        verify(cloudinaryService).delete("existing-id");
        verify(cloudinaryService).uploadSingle(any(File.class), anyMap());
        assertEquals("https://res.cloudinary.com/demo/image/upload/new.webp", result.get("Imagen"));
    }
}
