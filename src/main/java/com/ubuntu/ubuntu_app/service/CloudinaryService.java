package com.ubuntu.ubuntu_app.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubuntu.ubuntu_app.configuration.CloudinaryConfiguration;
import com.ubuntu.ubuntu_app.model.dto.CloudinaryResponseDTO;

@Component
public class CloudinaryService {

    @Autowired
    private CloudinaryConfiguration cloudinaryConfiguration;
    @Autowired
    private ObjectMapper objectMapper;

    public void multiUploadCloudinary(File[] file, Map<String, String> listOfUrl) throws IOException {
        for (int i = 1; i < file.length + 1; i++) {
            if (file[i - 1] != null) {
                String fileName = FilenameUtils.getBaseName(file[i - 1].getName());
                var cloudinaryUrl = cloudinaryConfiguration.getInstance()
                        .uploader()
                        .upload(file[i - 1], ObjectUtils.asMap("public_id", "ubuntu/" + fileName.trim()))
                        .get("secure_url").toString();
                listOfUrl.put("Imagen " + i, cloudinaryUrl);
                file[i - 1].delete();
            }
        }
    }

    public void singleUploadCloudinary(File file, Map<String, String> listOfUrl) throws IOException {
        if (file != null) {
            String fileName = FilenameUtils.getBaseName(file.getName());
            var cloudinaryUrl = cloudinaryConfiguration.getInstance()
                    .uploader()
                    .upload(file, ObjectUtils.asMap("public_id", "ubuntu/" + fileName.trim()))
                    .get("secure_url").toString();
            listOfUrl.put("Imagen", cloudinaryUrl);
            file.delete();
        }
    }

    public CloudinaryResponseDTO deleteFileFromCloudinary(String public_id) throws IOException {
        var response = cloudinaryConfiguration.getInstance()
                .uploader()
                .destroy(public_id, ObjectUtils.emptyMap());
        String json = writeJson(response);
        var value = deserializeResponse(json);
        if (value == null) {
            throw new RuntimeException("Null pointer");
        }
        return value;
    }

    public String writeJson(Map<?, ?> cloudinaryResponse) {
        try {
            return objectMapper.writeValueAsString(cloudinaryResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CloudinaryResponseDTO deserializeResponse(String cloudinaryResponse) {
        try {
            return objectMapper.readValue(cloudinaryResponse, CloudinaryResponseDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
