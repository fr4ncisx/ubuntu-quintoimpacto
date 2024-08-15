package com.ubuntu.ubuntu_app.infra.errors;

import java.nio.file.InvalidPathException;
import java.util.HashMap;
import java.util.Map;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<?> duplicatedKeyError(PSQLException ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex.getMessage().contains("ERROR: duplicate key value violates unique constraint")) {
            errors.put("error", "Error de clave duplicada");
            errors.put("message", "Database ServerSide problem");
        }
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> emptyBodyOrBadJson(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex.getMessage().contains("Required request body is missing")) {
            errors.put("error", "body vacio");
        }
        if (ex.getMessage().contains("was expecting comma to separate")) {
            errors.put("error", "Falta coma para separar algunos de los atributos");
        }
        if (ex.getMessage().contains("expected close marker for Object")) {
            errors.put("error", "Se espera cierre de llave al final");
        }
        if (ex.getMessage()
                .contains("Cannot deserialize value of type `com.ubuntu.ubuntu_app.model.enums.CategoryEnum`")) {
            errors.put("error", "La categoría no existe");
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        if (ex.getMessage().contains("Cannot deserialize value of type")) {
            errors.put("error", "Hubo un error al deserializar, se esperaba solo un objeto y no un arreglo");
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        if (ex.getMessage().contains("Cannot construct instance of")) {
            errors.put("error", "Error de formato en la solicitud");
        }
        if (ex.getMessage()
                .contains("Cannot coerce empty String (\"\") to `com.ubuntu.ubuntu_app.model.enums.CategoryEnum`")) {
            errors.put("error", "El campo 'categoría' no puede estar vacío");
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> mismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "error en el parámetro " + ex.getPropertyName());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SqlEmptyResponse.class)
    public ResponseEntity<?> emptyResponse(SqlEmptyResponse ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMensaje());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<?> emptyField(EmptyFieldException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMensaje());
        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<?> emailNotFound(EmailNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMensaje());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtInvalidTokenException.class)
    public ResponseEntity<?> invalidJWT(JwtInvalidTokenException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMensaje());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalParameterException.class)
    public ResponseEntity<?> nullTokenException(IllegalParameterException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMensaje());
        return new ResponseEntity<>(errors, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationErrors(MethodArgumentNotValidException ex) {
        var listOfErrors = ex.getFieldErrors().stream()
                .map(e -> new ShowFieldErrors(e.getField(), e.getDefaultMessage()));
        return new ResponseEntity<>(listOfErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> maximumUploadFileException(MaxUploadSizeExceededException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> maximumUploadFileException(MissingServletRequestPartException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", "Se requiere al menos una imagen para subir");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> parameterNotPresent(MissingServletRequestParameterException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", "Se esperaba un parámetro " + ex.getParameterType() + " '" + ex.getParameterName()
                + "' pero no se envio ningun parámetro con ese nombre");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalRewriteException.class)
    public ResponseEntity<?> unwrittableException(IllegalRewriteException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMensaje());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOExtensionException.class)
    public ResponseEntity<?> invalidFileExtension(IOExtensionException ex) {
        var showErrors = ex.getListOfErrors().stream()
                .map(e -> new ShowExtensionErrors(e.archivo(), e.error(), e.extension()));
        return new ResponseEntity<>(showErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> fileNotFoundToUpload(FileNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMensaje());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileExceededException.class)
    public ResponseEntity<?> exceededMaxLimit(FileExceededException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMensaje());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CloudinaryFileNotFoundException.class)
    public ResponseEntity<?> cloudinaryFileNotFound(CloudinaryFileNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMensaje());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<?> invalidPath(InvalidPathException ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex.getMessage().contains("Malformed input or input contains unmappable characters")) {
            errors.put("Error", "File name contains invalid characters, please fix the file name");
            System.out.println(ex.getMessage());
        } else {
            errors.put("Error", "Invalid path check console errors");
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private record ShowFieldErrors(String campo, String error) {
    }

    private record ShowExtensionErrors(String archivo, String error, String extension) {
    }
}
