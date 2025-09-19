package com.bytescolab.featureflag.controller;

import com.bytescolab.featureflag.config.security.config.SecurityConfig;
import com.bytescolab.featureflag.config.security.jwt.JwtFilter;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureConfigCreateRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.model.enums.Environment;
import com.bytescolab.featureflag.service.feature.FeatureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = FeatureController.class,
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
class FeatureControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private FeatureService featureService;

    @Test
    void createFeature_Returns201_WithBody() throws Exception {
        FeatureCreateRequestDTO req = FeatureCreateRequestDTO.builder()
                .name("dark_mode")
                .description("Modo oscuro")
                .enabledByDefault(true)
                .configs(List.of(
                        FeatureConfigCreateRequestDTO.builder()
                                .environment(Environment.DEV)
                                .enabled(true)
                                .build()
                ))
                .build();

        FeatureDetailResponseDTO res = FeatureDetailResponseDTO.builder()
                .id(UUID.randomUUID())
                .name("dark_mode")
                .description("Modo oscuro")
                .enabledByDefault(true)
                .build();

        when(featureService.createFeature(any(FeatureCreateRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/features")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("dark_mode"));

        verify(featureService).createFeature(any(FeatureCreateRequestDTO.class));
    }

    @Test
    void getAllFeatures_Empty_Returns204() throws Exception {
        when(featureService.getAllFeatures(null, null)).thenReturn(emptyList());

        mockMvc.perform(get("/api/features"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllFeatures_WithData_Returns200_List() throws Exception {
        var summary = FeatureSummaryResponseDTO.builder()
                .id(UUID.randomUUID())
                .name("dark_mode")
                .description("Modo oscuro")
                .build();

        when(featureService.getAllFeatures(null, null)).thenReturn(List.of(summary));

        mockMvc.perform(get("/api/features"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("dark_mode"));
    }

    @Test
    void getFeatureById_Returns200_Detail() throws Exception {
        UUID id = UUID.randomUUID();
        var detail = FeatureDetailResponseDTO.builder()
                .id(id).name("dark_mode").description("Modo oscuro").build();

        when(featureService.getFeatureById(id)).thenReturn(detail);

        mockMvc.perform(get("/api/features/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void enableFeature_Returns200_TextOK() throws Exception {
        UUID id = UUID.randomUUID();
        var req = FeatureActivationRequestDTO.builder()
                .environment(Environment.DEV)
                .clientId("acme")
                .enabled(true)
                .build();

        when(featureService.enableFeatureForClientOrEnv(eq(id), any())).thenReturn("OK");

        mockMvc.perform(post("/api/features/{id}/enable", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));

        verify(featureService).enableFeatureForClientOrEnv(eq(id), any());
    }

    @Test
    void disableFeature_Returns200_TextOK() throws Exception {
        UUID id = UUID.randomUUID();
        var req = FeatureActivationRequestDTO.builder()
                .environment(Environment.DEV)
                .clientId("acme")
                .enabled(false)
                .build();

        when(featureService.disableFeatureForClientOrEnv(eq(id), any())).thenReturn("OK");

        mockMvc.perform(post("/api/features/{id}/disable", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void checkFeature_Returns200_boolean() throws Exception {
        UUID fid = UUID.randomUUID();
        when(featureService.isFeatureActived(eq(fid), eq("acme"), eq(Environment.DEV))).thenReturn(true);

        mockMvc.perform(get("/api/features/check")
                        .param("featureId", fid.toString())
                        .param("clientId", "acme")
                        .param("env", "DEV"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
