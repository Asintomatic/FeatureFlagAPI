package com.bytescolab.featureflag.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() throws Exception {
        handler = new GlobalExceptionHandler();

        Field field = GlobalExceptionHandler.class.getDeclaredField("activeProfile");
        field.setAccessible(true);
        field.set(handler, "dev");
    }

    @Test
    void handleApiException_returnsProperResponse() {
        ApiException ex = new ApiException(ErrorCodes.USER_EXISTS, ErrorCodes.USER_EXISTS_MSG);

        ResponseEntity<Object> response = handler.handleApiException(
                ex,
                new ServletWebRequest(new MockHttpServletRequest())
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Object bodyObj = response.getBody();
        assertNotNull(bodyObj);
        assertInstanceOf(Map.class, bodyObj);

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) bodyObj;

        assertEquals(ErrorCodes.USER_EXISTS, body.get("code"));
        assertEquals(ErrorCodes.USER_EXISTS_MSG, body.get("message"));
    }

    @Test
    void handleValidation_returnsFieldErrorsInNonProd() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "test");
        bindingResult.addError(new FieldError("test", "username", "must not be blank"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Object> response = handler.handleValidation(
                ex,
                new ServletWebRequest(new MockHttpServletRequest())
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Object bodyObj = response.getBody();
        assertNotNull(bodyObj);
        assertInstanceOf(Map.class, bodyObj);

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) bodyObj;

        assertEquals(ErrorCodes.BAD_PARAMS, body.get("code"));
        assertTrue(((Map<?, ?>) body.get("errors")).containsKey("username"));
    }

    @Test
    void handleGenericException_returnsBadParams() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<Object> response = handler.handleGenericException(
                ex,
                new ServletWebRequest(new MockHttpServletRequest())
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Object bodyObj = response.getBody();
        assertNotNull(bodyObj);
        assertInstanceOf(Map.class, bodyObj);

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) bodyObj;

        assertEquals(ErrorCodes.BAD_PARAMS, body.get("code"));
    }
}
