package com.bytescolab.featureflag.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/api/test");
    }

    @Test
    void handleApiException_returnsCustomBody() {
        ApiException ex = new ApiException("AUTH_001", "Invalid token");
        ResponseEntity<Object> response = handler.handleApiException(ex, request);

        assertEquals(401, response.getStatusCode().value());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("AUTH_001", body.get("code"));
        assertEquals("Invalid token", body.get("message"));
    }

    @Test
    void handleValidation_returnsErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "test");
        bindingResult.addError(new FieldError("test", "field1", "must not be null"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Object> response = handler.handleValidation(ex);
        assertEquals(400, response.getStatusCode().value());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertTrue(((Map<?, ?>) body.get("errors")).containsKey("field1"));
    }

    @Test
    void handleIllegalArg_returnsBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid param");
        ResponseEntity<Object> response = handler.handleIllegalArg(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid param", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void handleRuntimeException_returnsInternalServerError() {
        RuntimeException ex = new RuntimeException("Unexpected error");
        ResponseEntity<Object> response = handler.handleRuntimeException(ex, request);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Unexpected error", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void handleBadCredentials_returnsUnauthorized() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");
        ResponseEntity<Object> response = handler.handleBadCredentials(ex, request);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Bad credentials", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void handleHttpMessageNotReadable_withInvalidFormat_returnsCustomMessage() {
        InvalidFormatException cause = new InvalidFormatException(null, "Invalid enum", "XXX", TestEnum.class);
        cause.prependPath(new Object(), "role");
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("JSON parse error", cause);

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(ex, request);

        assertEquals(400, response.getStatusCode().value());
        String msg = (String) ((Map<?, ?>) response.getBody()).get("message");
        assertTrue(msg.contains("role"));
        assertTrue(msg.contains("Valores permitidos"));
    }

    @Test
    void handleHttpMessageNotReadable_withOtherCause_returnsGenericBadRequest() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Malformed JSON", new Throwable("generic"));

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(ex, request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("JSON mal formado o incompatible", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void handleAccessDenied_returnsForbidden() {
        AccessDeniedException ex = new AccessDeniedException("Denied");
        ResponseEntity<Object> response = handler.handleAccessDenied(ex, request);

        assertEquals(403, response.getStatusCode().value());
        assertEquals("Acceso denegado: no tienes permisos suficientes", ((Map<?, ?>) response.getBody()).get("message"));
    }

    enum TestEnum { ADMIN, USER }
}
