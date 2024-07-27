package com.ubuntu.ubuntu_app.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ubuntu.ubuntu_app.infra.errors.FileExtensionRecord;
import com.ubuntu.ubuntu_app.infra.errors.IOExtensionException;

@Component
public class FileService {
    
    public Map<String, String> getExtension(MultipartFile file) {
        Map<String, String> extensions = new HashMap<>();
        if(file != null){
            extensions.put(FilenameUtils.getBaseName(file.getOriginalFilename()), FilenameUtils.getExtension(file.getOriginalFilename()));
        }        
        return extensions;
    }

    public Map<String, String> getAllExtensions(MultipartFile[] file) {
        Map<String, String> extensions = new HashMap<>();
        for (MultipartFile element : file) {
            if (element != null) {
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
            switch (fileMap.getValue()) {
                case "png" -> {
                    validationOK = true;
                    break;
                }
                case "jpg" -> {
                    validationOK = true;
                    break;
                }
                case "jpeg" -> {
                    validationOK = true;
                    break;
                }
                case "webp" -> {
                    validationOK = true;
                    break;
                }
                default -> listOfBadFiles.add(new FileExtensionRecord("Extensión inválida",
                        fileMap.getValue().toUpperCase(), fileMap.getKey()));
            }
        }
        if (!listOfBadFiles.isEmpty()) {
            throw new IOExtensionException(listOfBadFiles);
        }
        return validationOK;
    }

    public File[] convertMultipleFiles(MultipartFile[] multipartFile) throws IOException {
        int amount = multipartFile.length;
        File[] file = new File[amount];
        for (int i = 0; i < file.length; i++) {
            file[i] = new File(multipartFile[i].getOriginalFilename());
            FileUtils.writeByteArrayToFile(file[i], multipartFile[i].getBytes());
        }
        return file;
    }

}
