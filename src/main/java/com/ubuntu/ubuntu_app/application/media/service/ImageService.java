package com.ubuntu.ubuntu_app.application.media.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ubuntu.ubuntu_app.application.media.port.in.ImageUseCase;
import com.ubuntu.ubuntu_app.shared.error.CloudinaryFileNotFoundException;
import com.ubuntu.ubuntu_app.shared.error.EmptyFieldException;
import com.ubuntu.ubuntu_app.shared.error.FileExceededException;
import com.ubuntu.ubuntu_app.shared.error.FileNotFoundException;
import com.ubuntu.ubuntu_app.application.media.api.CloudinaryResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService implements ImageUseCase {

    private final FileService fileService;
    private final CloudinaryService cloudinaryService;

    public Map<String, String> upload(MultipartFile[] images) throws IOException {
        if (images.length == 0) {
            throw new FileNotFoundException("Se require al menos una imagen para subir a la plataforma");
        }
        if (images.length > 3) {
            throw new FileExceededException(
                    "Se excedió el limite de 3 archivos, se ingresaron " + images.length + " archivos");
        }
        var extensions = fileService.extractAllExtensions(images);
        if (!fileService.hasValidExtension(extensions)) {
            throw new FileExceededException("Ningún archivo tiene una extensión válida");
        }
        Map<String, String> cloudinaryURLs = new LinkedHashMap<>();
        var convertedToFile = fileService.convertAllToTempFiles(images);
        cloudinaryService.uploadAll(convertedToFile, cloudinaryURLs);
        return cloudinaryURLs;
    }

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
    public Map<String, String> replace(MultipartFile image, String publicId) throws IOException {
        if (image == null) {
            throw new FileNotFoundException("Missing 'image' parameter or undefined");
        }
        if (publicId == null) {
            throw new EmptyFieldException("Missing 'public_id' parameter");
        }
        var extensionObtained = fileService.extractExtension(image);
        if (!fileService.hasValidExtension(extensionObtained)) {
            throw new FileExceededException(
                    "Hubo un error al validar la extensión, comunicate con un desarrollador");
        }
        var response = cloudinaryService.delete(publicId);
        if (response.result().equals("not found")) {
            throw new CloudinaryFileNotFoundException("El public_id: '" + publicId + "' no existe en Cloudinary");
        }
        if (!response.result().equals("ok")) {
            throw new IOException("Cloudinary no pudo eliminar la imagen: " + response.result());
        }
        Map<String, String> responseURL = new LinkedHashMap<>();
        File file = fileService.convertToTempFile(image);
        try {
            cloudinaryService.uploadSingle(file, responseURL);
        } finally {
            file.delete();
        }
        return responseURL;
    }

}
