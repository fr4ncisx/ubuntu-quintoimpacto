package com.ubuntu.ubuntu_app.shared.error;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.mockito.Mockito;

class GlobalErrorHandlerTest {

    private final GlobalErrorHandler handler = new GlobalErrorHandler();

    @Test
    void unexpectedExceptionReturnsGenericInternalError() {
        var response = handler.unexpected(new IllegalStateException("db pool exhausted at 10.0.0.5"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().get("error"));
    }

    @Test
    void nullPointerExceptionDoesNotLeakDetails() {
        var response = handler.unexpected(new NullPointerException());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().get("error").contains("NullPointerException"));
    }

    @Test
    void malformedJsonReturnsBadRequest() {
        var ex = new HttpMessageNotReadableException("Required request body is missing",
                Mockito.mock(org.springframework.http.HttpInputMessage.class));

        var response = handler.emptyBodyOrBadJson(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void nonDuplicatePostgresErrorReturnsGenericMessage() {
        var ex = new PSQLException("FATAL: password authentication failed for user \"admin\"", PSQLState.CONNECTION_FAILURE);

        var response = handler.duplicatedKeyError(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Database ServerSide problem", response.getBody().get("error"));
    }

    @Test
    void duplicateKeyPostgresErrorReturnsConflict() {
        var ex = new PSQLException(
                "ERROR: duplicate key value violates unique constraint \"uq_users_email\"",
                PSQLState.UNIQUE_VIOLATION);

        var response = handler.duplicatedKeyError(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Error de clave duplicada", response.getBody().get("error"));
    }
}
