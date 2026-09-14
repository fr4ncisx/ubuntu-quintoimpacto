package com.ubuntu.ubuntu_app.application.media.port.in;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ubuntu.ubuntu_app.application.media.api.CloudinaryResult;

public interface ImageUseCase {

    Map<String, String> upload(MultipartFile[] images) throws IOException;

    CloudinaryResult delete(String publicId) throws IOException;

    Map<String, String> replace(MultipartFile image, String publicId) throws IOException;
}
