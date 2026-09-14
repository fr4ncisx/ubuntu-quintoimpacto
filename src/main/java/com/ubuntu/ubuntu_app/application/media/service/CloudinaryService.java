package com.ubuntu.ubuntu_app.application.media.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubuntu.ubuntu_app.application.media.api.CloudinaryResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private static final int URL_FETCH_CONNECT_TIMEOUT_MS = 10000;
    private static final int URL_FETCH_READ_TIMEOUT_MS = 10000;

    private final Cloudinary cloudinary;
    private final ObjectMapper objectMapper;
    private static final String SECURE_URL = "secure_url";

    public void uploadAll(File[] file, Map<String, String> listOfUrl) throws IOException {
        for (int i = 1; i < file.length + 1; i++) {
            if (file[i - 1] != null) {
                String fileName = FilenameUtils.getBaseName(file[i - 1].getName());
                try {
                    var cloudinaryUrl = cloudinary
                            .uploader()
                            .upload(file[i - 1], ObjectUtils.asMap("public_id", "ubuntu/" + fileName.trim(),
                                    "overwrite", false, "unique_filename", true))
                            .get(SECURE_URL)
                            .toString();
                    listOfUrl.put("Imagen " + i, cloudinaryUrl);
                } finally {
                    file[i - 1].delete();
                }
            }
        }
    }

    public void uploadSingle(File file, Map<String, String> listOfUrl) throws IOException {
        if (file != null) {
            String fileName = FilenameUtils.getBaseName(file.getName());
            try {
                var cloudinaryUrl = cloudinary
                        .uploader()
                        .upload(file, ObjectUtils.asMap("public_id", "ubuntu/" + fileName.trim(),
                                "overwrite", false, "unique_filename", true))
                        .get(SECURE_URL).toString();
                listOfUrl.put("Imagen", cloudinaryUrl);
            } finally {
                file.delete();
            }
        }
    }

    public CloudinaryResult delete(String publicId) throws IOException {
        var response = cloudinary
                .uploader()
                .destroy(publicId, ObjectUtils.emptyMap());
        String json = toJson(response);
        var value = fromJson(json);
        if (value == null) {
            throw new IOException("No se pudo interpretar la respuesta de Cloudinary");
        }
        return value;
    }

    public String uploadProfilePhoto(Object googleURL, int size) throws IOException, URISyntaxException {
        if (googleURL == null) {
            throw new IOException("URL de imagen nula");
        }
        var googleImgURL = new URI(googleURL.toString().replace("s96-c", "s" + size + "-c")).toURL();
        if (!"https".equalsIgnoreCase(googleImgURL.getProtocol())) {
            throw new IOException("Esquema de URL no permitido");
        }
        File file = File.createTempFile("profile-", ".jpg");
        try {
            FileUtils.copyURLToFile(googleImgURL, file, URL_FETCH_CONNECT_TIMEOUT_MS, URL_FETCH_READ_TIMEOUT_MS);
            String fileBaseName = FilenameUtils.getBaseName(file.getName());
            return cloudinary
                    .uploader()
                    .upload(file, ObjectUtils.asMap("public_id", "ubuntu/" + fileBaseName.trim(),
                            "overwrite", false, "unique_filename", true)).get(SECURE_URL)
                    .toString();
        } finally {
            file.delete();
        }
    }

    private String toJson(Map<?, ?> cloudinaryResponse) throws IOException {
        try {
            return objectMapper.writeValueAsString(cloudinaryResponse);
        } catch (JsonProcessingException e) {
            log.warn("No se pudo serializar la respuesta de Cloudinary", e);
            throw new IOException("No se pudo serializar la respuesta de Cloudinary", e);
        }
    }

    private CloudinaryResult fromJson(String cloudinaryResponse) throws IOException {
        try {
            return objectMapper.readValue(cloudinaryResponse, CloudinaryResult.class);
        } catch (JsonProcessingException e) {
            log.warn("No se pudo deserializar la respuesta de Cloudinary", e);
            throw new IOException("No se pudo deserializar la respuesta de Cloudinary", e);
        }
    }
}
