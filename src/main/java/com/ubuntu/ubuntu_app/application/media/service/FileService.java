package com.ubuntu.ubuntu_app.application.media.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ubuntu.ubuntu_app.shared.error.FileExtensionRecord;
import com.ubuntu.ubuntu_app.shared.error.IOExtensionException;

@Service
public class FileService {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("png", "jpg", "jpeg", "webp");

    public Map<String, String> extractExtension(MultipartFile file) {
        Map<String, String> extensions = new HashMap<>();
        if (file != null && file.getOriginalFilename() != null) {
            extensions.put(FilenameUtils.getBaseName(file.getOriginalFilename()), FilenameUtils.getExtension(file.getOriginalFilename()));
        }        
        return extensions;
    }

    public Map<String, String> extractAllExtensions(MultipartFile[] file) {
        Map<String, String> extensions = new HashMap<>();
        for (MultipartFile element : file) {
            if (element != null && element.getOriginalFilename() != null) {
                extensions.put(FilenameUtils.getBaseName(element.getOriginalFilename()),
                        FilenameUtils.getExtension(element.getOriginalFilename()));
            }
        }
        return extensions;
    }

    public boolean hasValidExtension(Map<String, String> extensionsMap) throws IOExtensionException {
        List<FileExtensionRecord> listOfBadFiles = new ArrayList<>();
        boolean validationOK = false;
        for (Entry<String, String> fileMap : extensionsMap.entrySet()) {
            String extension = fileMap.getValue() == null ? "" : fileMap.getValue().toLowerCase();
            switch (extension) {
                case "png","jpg","jpeg","webp" -> validationOK = true;           
                default -> listOfBadFiles.add(new FileExtensionRecord("Extensión inválida",
                        extension.toUpperCase(), fileMap.getKey()));
            }
        }
        if (!listOfBadFiles.isEmpty()) {
            throw new IOExtensionException(listOfBadFiles);
        }
        return validationOK;
    }

    public File[] convertAllToTempFiles(MultipartFile[] multipartFile) throws IOException {
        File[] files = new File[multipartFile.length];
        for (int i = 0; i < multipartFile.length; i++) {
            files[i] = convertToTempFile(multipartFile[i]);
        }
        return files;
    }

    public File convertToTempFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename == null ? ""
                : FilenameUtils.getExtension(originalFilename).toLowerCase();
        String suffix = ALLOWED_EXTENSIONS.contains(extension) ? "." + extension : ".bin";
        Path tempFile = Files.createTempFile("upload-", suffix);
        multipartFile.transferTo(tempFile);
        return tempFile.toFile();
    }

}
