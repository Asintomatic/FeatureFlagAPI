package com.bytescolab.featureflag.mapper;

import com.bytescolab.featureflag.repository.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureConfigCreateRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureConfigResponseDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;
import com.bytescolab.featureflag.model.enums.Environment;
import com.bytescolab.featureflag.repository.mapper.FeatureMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FeatureMapperTest {

    @Test
    void toEntity_withoutConfigs_mapsBasicFields() {
        FeatureCreateRequestDTO dto = FeatureCreateRequestDTO.builder()
                .name("flag-1")
                .description("test desc")
                .enabledByDefault(true)
                .build();

        Feature feature = FeatureMapper.toEntity(dto);

        assertEquals("flag-1", feature.getName());
        assertEquals("test desc", feature.getDescription());
        assertTrue(feature.getEnabledByDefault());
        assertNull(feature.getConfigs());
    }

    @Test
    void toEntity_withConfigs_mapsConfigs() {
        FeatureConfigCreateRequestDTO cfgDto = FeatureConfigCreateRequestDTO.builder()
                .environment(Environment.DEV)
                .clientId("clientA")
                .enabled(true)
                .build();

        FeatureCreateRequestDTO dto = FeatureCreateRequestDTO.builder()
                .name("flag-2")
                .description("desc")
                .enabledByDefault(false)
                .configs(List.of(cfgDto))
                .build();

        Feature feature = FeatureMapper.toEntity(dto);

        assertEquals("flag-2", feature.getName());
        assertEquals(1, feature.getConfigs().size());
        FeatureConfig cfg = feature.getConfigs().get(0);
        assertEquals(Environment.DEV, cfg.getEnvironment());
        assertEquals("clientA", cfg.getClientId());
        assertTrue(cfg.getEnabled());
        assertEquals(feature, cfg.getFeature());
    }

    @Test
    void toDetailResponseDTO_mapsFeatureAndConfigs() {
        Feature feature = Feature.builder()
                .id(UUID.randomUUID())
                .name("flag-3")
                .description("detail desc")
                .enabledByDefault(true)
                .configs(List.of(FeatureConfig.builder()
                        .id(UUID.randomUUID())
                        .environment(Environment.PROD)
                        .clientId("clientB")
                        .enabled(false)
                        .build()))
                .build();

        FeatureDetailResponseDTO dto = FeatureMapper.toDetailResponseDTO(feature);

        assertEquals(feature.getId(), dto.getId());
        assertEquals("flag-3", dto.getName());
        assertEquals("detail desc", dto.getDescription());
        assertTrue(dto.getEnabledByDefault());
        assertEquals(1, dto.getConfigs().size());
        assertEquals("clientB", dto.getConfigs().get(0).getClientId());
    }

    @Test
    void toSummaryDTO_mapsBasicFields() {
        Feature feature = Feature.builder()
                .id(UUID.randomUUID())
                .name("flag-4")
                .description("summary desc")
                .build();

        FeatureSummaryResponseDTO dto = FeatureMapper.toSummaryDTO(feature);

        assertEquals(feature.getId(), dto.getId());
        assertEquals("flag-4", dto.getName());
        assertEquals("summary desc", dto.getDescription());
    }

    @Test
    void toConfigDTO_mapsFields() {
        FeatureConfig cfg = FeatureConfig.builder()
                .id(UUID.randomUUID())
                .environment(Environment.STAGING)
                .clientId("clientC")
                .enabled(true)
                .build();

        FeatureConfigResponseDTO dto = FeatureMapper.toConfigDTO(cfg);

        assertEquals(cfg.getId(), dto.getId());
        assertEquals(Environment.STAGING, dto.getEnvironment());
        assertEquals("clientC", dto.getClientId());
        assertTrue(dto.getEnabled());
    }

    @Test
    void toConfigEntity_fromConfigCreateDTO_mapsFields() {
        Feature feature = Feature.builder().id(UUID.randomUUID()).name("flag-x").build();

        FeatureConfigCreateRequestDTO dto = FeatureConfigCreateRequestDTO.builder()
                .environment(Environment.DEV)
                .clientId("clientD")
                .enabled(false)
                .build();

        FeatureConfig cfg = FeatureMapper.toConfigEntity(dto, feature);

        assertEquals(Environment.DEV, cfg.getEnvironment());
        assertEquals("clientD", cfg.getClientId());
        assertFalse(cfg.getEnabled());
        assertEquals(feature, cfg.getFeature());
    }

    @Test
    void toConfigEntity_fromActivationDTO_defaultsEnabledTrueIfNull() {
        Feature feature = Feature.builder().id(UUID.randomUUID()).name("flag-y").build();

        FeatureActivationRequestDTO dto = FeatureActivationRequestDTO.builder()
                .environment(Environment.PROD)
                .clientId("clientE")
                .enabled(null)
                .build();

        FeatureConfig cfg = FeatureMapper.toConfigEntity(dto, feature);

        assertEquals(Environment.PROD, cfg.getEnvironment());
        assertEquals("clientE", cfg.getClientId());
        assertTrue(cfg.getEnabled());
        assertEquals(feature, cfg.getFeature());
    }
}
