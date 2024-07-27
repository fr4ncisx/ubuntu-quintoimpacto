package com.ubuntu.ubuntu_app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ubuntu.ubuntu_app.service.ImageService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cloudinary")
@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> fileUploader(@RequestPart(required = true) MultipartFile[] images) throws IOException {
        return imageService.uploadImages(images);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestParam(required = true) String public_id) throws IOException {
        return imageService.deleteImage(public_id);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editImage(@RequestPart(required = true) MultipartFile image,
            @RequestParam(required = true) String public_id) throws IOException {
        return imageService.editImage(image, public_id);
    }
}
