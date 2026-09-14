package com.ubuntu.ubuntu_app.shared.error;

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
    public ResponseEntity<Map<String, String>> duplicatedKeyError(PSQLException ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex.getMessage() != null && ex.getMessage().contains("ERROR: duplicate key value violates unique constraint")) {
            errors.put("error", "Error de clave duplicada");
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
        }
        errors.put("error", "Database ServerSide problem");
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> emptyBodyOrBadJson(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        String message = ex.getMessage() == null ? "" : ex.getMessage();
        if (message.contains("Required request body is missing")) {
            errors.put("error", "body vacio");
        }
        if (message.contains("was expecting comma to separate")) {
            errors.put("error", "Falta coma para separar algunos de los atributos");
        }
        if (message.contains("expected close marker for Object")) {
            errors.put("error", "Se espera cierre de llave al final");
        }
        if (message.contains("Cannot deserialize value of type")) {
            errors.put("error", "Hubo un error al deserializar, se esperaba solo un objeto y no un arreglo");
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        if (message.contains("Cannot construct instance of")) {
            errors.put("error", "Error de formato en la solicitud");
        }
        if (errors.isEmpty()) {
            errors.put("error", "Solicitud mal formada");
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> mismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "error en el parámetro " + ex.getPropertyName());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SqlEmptyResponse.class)
    public ResponseEntity<Map<String, String>> emptyResponse(SqlEmptyResponse ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<Map<String, String>> emptyField(EmptyFieldException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Map<String, String>> emailNotFound(EmailNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtInvalidTokenException.class)
    public ResponseEntity<Map<String, String>> invalidJWT(JwtInvalidTokenException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalParameterException.class)
    public ResponseEntity<Map<String, String>> nullTokenException(IllegalParameterException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<java.util.List<ShowFieldErrors>> validationErrors(MethodArgumentNotValidException ex) {
        var listOfErrors = ex.getFieldErrors().stream()
                .map(e -> new ShowFieldErrors(e.getField(), e.getDefaultMessage())).toList();
        return new ResponseEntity<>(listOfErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<java.util.List<ShowFieldErrors>> constraintViolations(
            jakarta.validation.ConstraintViolationException ex) {
        var listOfErrors = ex.getConstraintViolations().stream()
                .map(v -> new ShowFieldErrors(v.getPropertyPath().toString(), v.getMessage())).toList();
        return new ResponseEntity<>(listOfErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> maximumUploadFileException(MaxUploadSizeExceededException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", "Se excedió el tamaño máximo de archivo permitido");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Map<String, String>> maximumUploadFileException(MissingServletRequestPartException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", "Se requiere al menos una imagen para subir");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> parameterNotPresent(MissingServletRequestParameterException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", "Se esperaba un parámetro " + ex.getParameterType() + " '" + ex.getParameterName()
                + "' pero no se envio ningun parámetro con ese nombre");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalRewriteException.class)
    public ResponseEntity<Map<String, String>> unwrittableException(IllegalRewriteException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOExtensionException.class)
    public ResponseEntity<java.util.List<ShowExtensionErrors>> invalidFileExtension(IOExtensionException ex) {
        var showErrors = ex.getListOfErrors().stream()
                .map(e -> new ShowExtensionErrors(e.fileName(), e.error(), e.extension())).toList();
        return new ResponseEntity<>(showErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Map<String, String>> fileNotFoundToUpload(FileNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileExceededException.class)
    public ResponseEntity<Map<String, String>> exceededMaxLimit(FileExceededException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CloudinaryFileNotFoundException.class)
    public ResponseEntity<Map<String, String>> cloudinaryFileNotFound(CloudinaryFileNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<Map<String, String>> invalidPath(InvalidPathException ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex.getMessage() != null && ex.getMessage().contains("Malformed input or input contains unmappable characters")) {
            errors.put("Error", "File name contains invalid characters, please fix the file name");
        } else {
            errors.put("Error", "Invalid path");
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GeocodeErrorException.class)
    public ResponseEntity<Map<String, String>> unableToGeocode(GeocodeErrorException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(HttpResponseErrorException.class)
    public ResponseEntity<Map<String, String>> httpResponseError(HttpResponseErrorException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> unexpected(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Error interno del servidor");
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private record ShowFieldErrors(String field, String message) {
    }

    private record ShowExtensionErrors(String file, String message, String extension) {
    }
}
