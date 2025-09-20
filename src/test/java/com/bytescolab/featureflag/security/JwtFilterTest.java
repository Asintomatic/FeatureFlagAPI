package com.bytescolab.featureflag.security;

import com.bytescolab.featureflag.config.security.jwt.JwtFilter;
import com.bytescolab.featureflag.config.security.jwt.JwtUtils;
import com.bytescolab.featureflag.exception.ApiException;
import com.bytescolab.featureflag.exception.ErrorCodes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_skipsAuthEndpoints() throws ServletException, IOException {
        request.setRequestURI("/api/auth/login");

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_noAuthorizationHeader() throws ServletException, IOException {
        request.setRequestURI("/api/feature");

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_validToken_authenticationSet() throws ServletException, IOException {
        request.setRequestURI("/api/feature");
        request.addHeader("Authorization", "Bearer valid-token");

        UserDetails userDetails = new User("pepe", "pass", Collections.emptyList());

        when(jwtUtils.extractUsername("valid-token")).thenReturn("pepe");
        when(userDetailsService.loadUserByUsername("pepe")).thenReturn(userDetails);
        when(jwtUtils.isTokenValid("valid-token", userDetails)).thenReturn(true);

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("pepe",
                ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @Test
    void doFilter_invalidToken_authenticationNotSet() throws ServletException, IOException {
        request.setRequestURI("/api/feature");
        request.addHeader("Authorization", "Bearer invalid-token");

        UserDetails userDetails = new User("pepe", "pass", Collections.emptyList());

        when(jwtUtils.extractUsername("invalid-token")).thenReturn("pepe");
        when(userDetailsService.loadUserByUsername("pepe")).thenReturn(userDetails);
        when(jwtUtils.isTokenValid("invalid-token", userDetails)).thenReturn(false);

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_tokenThrowsApiException_clearsContextAndRethrows() throws ServletException, IOException {
        request.setRequestURI("/api/feature");
        request.addHeader("Authorization", "Bearer expired-token");

        when(jwtUtils.extractUsername("expired-token"))
                .thenThrow(new ApiException(ErrorCodes.TOKEN_EXPIRADO, "Token expirado"));

        ApiException thrown = assertThrows(ApiException.class, () ->
                jwtFilter.doFilter(request, response, filterChain)
        );

        assertEquals(ErrorCodes.TOKEN_EXPIRADO, thrown.getCode());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
