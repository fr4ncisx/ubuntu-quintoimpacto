package com.ubuntu.ubuntu_app.shared.api;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(int status, String message, T data, Instant timestamp) {

    public static <T> ApiResponse<T> of(String message, T data, HttpStatus status) {
        return new ApiResponse<>(status.value(), message, data, Instant.now());
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of("OK", data, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> created(T data) {
        return of("Creado exitosamente", data, HttpStatus.CREATED);
    }

    public static ApiResponse<Void> noContent() {
        return of("Sin contenido", null, HttpStatus.NO_CONTENT);
    }

    public static <T> ApiResponse<T> message(String message, HttpStatus status) {
        return of(message, null, status);
    }
}
