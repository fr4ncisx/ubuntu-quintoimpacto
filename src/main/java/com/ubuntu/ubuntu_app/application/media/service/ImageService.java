package com.ubuntu.ubuntu_app.application.media.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ubuntu.ubuntu_app.application.media.api.CloudinaryResult;
import com.ubuntu.ubuntu_app.application.media.domain.MediaFormat;
import com.ubuntu.ubuntu_app.application.media.port.in.ImageUseCase;
import com.ubuntu.ubuntu_app.shared.error.CloudinaryFileNotFoundException;
import com.ubuntu.ubuntu_app.shared.error.FileExceededException;
import com.ubuntu.ubuntu_app.shared.error.FileNotFoundException;
import com.ubuntu.ubuntu_app.shared.error.SecurityValidationException;
import com.ubuntu.ubuntu_app.shared.support.TempFileResource;

@Service
public class ImageService implements ImageUseCase {

    private static final int MAX_FILES = 3;
    private static final long MAX_FILE_SIZE = 3 * 1024 * 1024;

    private final FormatRegistry formatRegistry;
    private final CloudinaryService cloudinaryService;

    public ImageService(FormatRegistry formatRegistry, CloudinaryService cloudinaryService) {
        this.formatRegistry = formatRegistry;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public Map<String, String> upload(MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            throw new FileNotFoundException("Se requiere al menos una imagen para subir");
        }
        if (files.length > MAX_FILES) {
            throw new FileExceededException("Se excedió el límite máximo de " + MAX_FILES + " imágenes por operación");
        }

        Map<String, String> uploadedUrls = new LinkedHashMap<>(files.length);

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            validateFileSecurity(file);

            String key = files.length == 1 ? "Imagen" : "Imagen " + (i + 1);

            try (TempFileResource tempResource = TempFileResource.fromStream(
                    file.getInputStream(), "ubuntu-upload-", ".tmp")) {
                Map<String, String> singleResult = new LinkedHashMap<>();
                cloudinaryService.uploadSingle(tempResource.toFile(), singleResult);
                uploadedUrls.put(key, singleResult.get("Imagen"));
            }
        }

        return uploadedUrls;
    }

    @Override
    public CloudinaryResult delete(String publicId) throws IOException {
        var response = cloudinaryService.delete(publicId);
        if (response.result().equals("not found")) {
            throw new CloudinaryFileNotFoundException("El public_id: '" + publicId + "' no existe en Cloudinary");
        }
        if (!response.result().equals("ok")) {
            throw new IOException("Cloudinary no pudo eliminar la imagen: " + response.result());
        }
        return response;
    }

    @Override
    public Map<String, String> replace(MultipartFile image, String publicId) throws IOException {
        if (image == null) {
            throw new FileNotFoundException("El parámetro de imagen es requerido");
        }
        validateFileSecurity(image);

        delete(publicId);

        Map<String, String> response = new LinkedHashMap<>();
        try (TempFileResource tempResource = TempFileResource.fromStream(
                image.getInputStream(), "ubuntu-replace-", ".tmp")) {
            cloudinaryService.uploadSingle(tempResource.toFile(), response);
        }
        return response;
    }

    private void validateFileSecurity(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new SecurityValidationException("El archivo proporcionado está vacío");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileExceededException("El archivo excede el tamaño máximo permitido de 3 MB");
        }

        String filename = file.getOriginalFilename();
        String extension = extractExtension(filename);
        MediaFormat declaredFormat = formatRegistry.resolveFormat(extension);

        try (InputStream stream = new BufferedInputStream(file.getInputStream())) {
            stream.mark(formatRegistry.maxRequiredHeader());
            byte[] header = new byte[formatRegistry.maxRequiredHeader()];
            int read = stream.read(header);
            if (read < formatRegistry.maxRequiredHeader()) {
                throw new SecurityValidationException("Contenido de archivo truncado");
            }
            formatRegistry.verifySignature(declaredFormat, header);
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new SecurityValidationException("Nombre de archivo sin extensión válida");
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
