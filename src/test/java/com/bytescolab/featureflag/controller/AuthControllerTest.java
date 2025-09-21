package com.bytescolab.featureflag.controller;

import com.bytescolab.featureflag.config.security.config.SecurityConfig;
import com.bytescolab.featureflag.config.security.jwt.JwtFilter;
import com.bytescolab.featureflag.repository.dto.auth.request.LoginRequestDTO;
import com.bytescolab.featureflag.repository.dto.auth.request.RegisterRequestDTO;
import com.bytescolab.featureflag.repository.dto.auth.response.AuthRegisterResponseDTO;
import com.bytescolab.featureflag.repository.dto.auth.response.AuthResponseDTO;
import com.bytescolab.featureflag.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        SecurityConfig.class,
                        JwtFilter.class
                }
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private AuthService authService;

    @Test
    void register_Successful_ReturnsCreated() throws Exception {
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("testuser")
                .password("Prueba1!")
                .build();

        AuthRegisterResponseDTO response = AuthRegisterResponseDTO.builder()
                .username("testuser")
                .role("USER")
                .build();

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(authService).register(any(RegisterRequestDTO.class));
    }

    @Test
    void login_Successful_ReturnsOkAndToken() throws Exception {
        // 1) Arrange: cuerpo válido de login
        LoginRequestDTO req = LoginRequestDTO.builder()
                .username("testuser")
                .password("Prueba1!")
                .build();

        // 2) Mock: respuesta del servicio con el token
        AuthResponseDTO res = AuthResponseDTO.builder()
                .accessToken("jwt-token-ejemplo")
                .tokenType("Bearer")
                .username("testuser")
                .role("USER")
                .expiresAt(999999L)
                .build();
        when(authService.login(any(LoginRequestDTO.class))).thenReturn(res);

        // 3) Act & Assert: POST /api/auth/login
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value("jwt-token-ejemplo"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"));

        // 4) Verificamos colaboración (inversión de dependencias)
        verify(authService).login(any(LoginRequestDTO.class));
    }
}