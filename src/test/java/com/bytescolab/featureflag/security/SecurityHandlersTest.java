package com.bytescolab.featureflag.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SecurityHandlersTest {

    private RestAuthenticationEntryPoint authenticationEntryPoint;
    private RestAccessDeniedHandler accessDeniedHandler;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        authenticationEntryPoint = new RestAuthenticationEntryPoint();
        accessDeniedHandler = new RestAccessDeniedHandler();
        mapper = new ObjectMapper();
    }

    @Test
    void commence_returnsUnauthorizedJsonResponse() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        MockHttpServletResponse response = new MockHttpServletResponse();

        AuthenticationException ex = mock(AuthenticationException.class);

        authenticationEntryPoint.commence(request, response, ex);

        assertEquals(401, response.getStatus());
        assertEquals("application/json", response.getContentType());

        Map body = mapper.readValue(response.getContentAsByteArray(), Map.class);
        assertEquals(401, body.get("status"));
        assertEquals("Unauthorized", body.get("error"));
        assertEquals("Authentication is required to access this resource", body.get("message"));
        assertEquals("/api/protected", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handle_returnsForbiddenJsonResponse() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/admin");
        MockHttpServletResponse response = new MockHttpServletResponse();

        AccessDeniedException ex = new AccessDeniedException("Forbidden");

        accessDeniedHandler.handle(request, response, ex);

        assertEquals(403, response.getStatus());
        assertEquals("application/json", response.getContentType());

        Map body = mapper.readValue(response.getContentAsByteArray(), Map.class);
        assertEquals(403, body.get("status"));
        assertEquals("Forbidden", body.get("error"));
        assertEquals("You do not have permission to access this resource", body.get("message"));
        assertEquals("/api/admin", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }
}
