package com.ubuntu.ubuntu_app.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ubuntu.ubuntu_app.infra.errors.CloudinaryFileNotFoundException;
import com.ubuntu.ubuntu_app.infra.errors.EmptyFieldException;
import com.ubuntu.ubuntu_app.infra.errors.FileExceededException;
import com.ubuntu.ubuntu_app.infra.errors.FileNotFoundException;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;

@Service
public class ImageService {

    @Autowired
    private FileService fileService;
    @Autowired
    private CloudinaryService cloudinaryService;

    public ResponseEntity<?> uploadImages(MultipartFile[] images) throws IOException {
        if (images.length == 0) {
            throw new FileNotFoundException("Se require al menos una imagen para subir a la plataforma");
        }
        if (images.length > 3) {
            throw new FileExceededException(
                    "Se excedió el limite de 3 archivos, se ingresaron " + images.length + " archivos");
        }
        var extensions = fileService.getAllExtensions(images);
        if (fileService.hasValidExtension(extensions)) {
            Map<String, String> cloudinaryURLs = new LinkedHashMap<>();
            var convertedToFile = fileService.convertMultipleFiles(images);
            cloudinaryService.multiUploadCloudinary(convertedToFile, cloudinaryURLs);
            return ResponseEntity.ok(cloudinaryURLs);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<?> deleteImage(String public_id) throws IOException {
        var response = cloudinaryService.deleteFileFromCloudinary(public_id);
        if (response.getResult().equals("ok")) {
            return ResponseEntity.ok(response);
        }
        if (response.getResult().equals("not found")) {
            throw new CloudinaryFileNotFoundException("El public_id: '" + public_id + "' no existe en Cloudinary");
        }
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 
     * 
     * Recibe como parámetro el public_id de la imagen de Cloudinary
     * <p>
     * Recibe como parámetro la nueva imagen que se va a subir
     * <p>
     * Se valida la extensión del archivo (image)
     * <p>
     * Una vez validado que sea una imagen se procede a borrar la imagen de
     * Cloudinary
     * <p>
     * Usando el 'public_id', si el public_id no se encuentra nos tirará una
     * excepción
     * <p>
     * Caso contrario (respuesta: ok) quiere decir que se eliminó de Cloudinary
     * <p>
     * Y procedemos a subir la nueva imagen ya previamente convertida a tipo File
     * <p>
     * Nos va a retornar un json con el link de la imagen donde el front la tiene
     * que cargar al editar el emprendimiento
     * 
     */
    public ResponseEntity<?> editImage(MultipartFile image, String public_id) throws IOException {
        if (image == null) {
            throw new FileNotFoundException("Missing 'image' parameter or undefined");
        }
        if (public_id == null) {
            throw new EmptyFieldException("Missing 'public_id' parameter");
        }
        var extensionObtained = fileService.getExtension(image);
        if (fileService.hasValidExtension(extensionObtained)) {
            var response = cloudinaryService.deleteFileFromCloudinary(public_id);
            if (response.getResult().equals("not found")) {
                throw new CloudinaryFileNotFoundException("El public_id: '" + public_id + "' no existe en Cloudinary");
            }
            if (response.getResult().equals("ok")) {
                Map<String, String> responseURL = new LinkedHashMap<>();
                File file = new File(image.getOriginalFilename());
                FileUtils.writeByteArrayToFile(file, image.getBytes());
                cloudinaryService.singleUploadCloudinary(file, responseURL);
                return ResponseEntity.ok(responseURL);
            }
        }
        return ResponseEntity.badRequest()
                .body(ResponseMap
                        .createResponse("Hubo un error al validar la extensión, comunicate con un desarrollador"));
    }
}
