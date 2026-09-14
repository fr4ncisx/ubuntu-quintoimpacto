package com.ubuntu.ubuntu_app.infrastructure.media.web;

import java.io.IOException;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.media.api.CloudinaryResult;
import com.ubuntu.ubuntu_app.application.media.port.in.ImageUseCase;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/cloudinary")
@RequiredArgsConstructor
public class CloudinaryV1Controller {

    private final ImageUseCase imageService;

    @PostMapping("/images")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImages(
            @RequestPart(required = true) MultipartFile[] images) throws IOException {
        return ResponseEntity.ok(ApiResponse.ok(imageService.upload(images)));
    }

    @DeleteMapping("/images")
    public ResponseEntity<ApiResponse<CloudinaryResult>> deleteImage(
            @RequestParam(required = true) String publicId) throws IOException {
        return ResponseEntity.ok(ApiResponse.ok(imageService.delete(publicId)));
    }

    @PutMapping("/images")
    public ResponseEntity<ApiResponse<Map<String, String>>> replaceImage(
            @RequestPart(required = true) MultipartFile image,
            @RequestParam(required = true) String publicId) throws IOException {
        return ResponseEntity.ok(ApiResponse.ok(imageService.replace(image, publicId)));
    }
}
