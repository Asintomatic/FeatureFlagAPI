package com.bytescolab.featureflag.service;

import com.bytescolab.featureflag.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.exception.ApiException;
import com.bytescolab.featureflag.exception.ErrorCodes;
import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;
import com.bytescolab.featureflag.model.enums.Environment;
import com.bytescolab.featureflag.repository.FeatureConfigRepository;
import com.bytescolab.featureflag.repository.FeatureRepository;
import com.bytescolab.featureflag.service.feature.FeatureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeatureServiceImplTest {

    @Mock
    private FeatureRepository featureRepository;

    @Mock
    private FeatureConfigRepository featureConfigRepository;

    @InjectMocks
    private FeatureServiceImpl service;

    private Feature feature;
    private UUID featureId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        featureId = UUID.randomUUID();
        feature = Feature.builder()
                .id(featureId)
                .name("flag-x")
                .description("desc")
                .enabledByDefault(true)
                .build();
    }

    @Test
    void createFeature_ok() {
        FeatureCreateRequestDTO dto = FeatureCreateRequestDTO.builder()
                .name("flag-x")
                .description("desc")
                .enabledByDefault(true)
                .build();

        when(featureRepository.existsByName("flag-x")).thenReturn(false);
        when(featureRepository.save(any(Feature.class))).thenReturn(feature);

        FeatureDetailResponseDTO res = service.createFeature(dto);

        assertEquals("flag-x", res.getName());
        verify(featureRepository).save(any(Feature.class));
    }

    @Test
    void createFeature_alreadyExists_throwsApiException() {
        FeatureCreateRequestDTO dto = FeatureCreateRequestDTO.builder().name("flag-x").build();

        when(featureRepository.existsByName("flag-x")).thenReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> service.createFeature(dto));
        assertEquals(ErrorCodes.FEATURE_EXISTS, ex.getCode());
    }

    @Test
    void getAllFeatures_noFilters_returnsAll() {
        when(featureRepository.findAll()).thenReturn(List.of(feature));

        List<FeatureSummaryResponseDTO> res = service.getAllFeatures(null, null);

        assertEquals(1, res.size());
        assertEquals("flag-x", res.get(0).getName());
    }

    @Test
    void getAllFeatures_withNameAndEnabled_found() {
        when(featureRepository.existsByName("flag-x")).thenReturn(true);
        when(featureRepository.findByEnabledByDefaultAndNameContainingIgnoreCase(true, "flag-x"))
                .thenReturn(List.of(feature));

        var res = service.getAllFeatures(true, "flag-x");

        assertEquals(1, res.size());
        verify(featureRepository).findByEnabledByDefaultAndNameContainingIgnoreCase(true, "flag-x");
    }

    @Test
    void getAllFeatures_withNameNotFound_throwsApiException() {
        when(featureRepository.existsByName("other")).thenReturn(false);

        ApiException ex = assertThrows(ApiException.class, () -> service.getAllFeatures(null, "other"));
        assertEquals(ErrorCodes.FEATURE_NOT_FOUND, ex.getCode());
    }

    @Test
    void getFeatureById_ok() {
        when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));

        var res = service.getFeatureById(featureId);

        assertEquals("flag-x", res.getName());
    }

    @Test
    void getFeatureById_notFound_throwsApiException() {
        when(featureRepository.findById(featureId)).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () -> service.getFeatureById(featureId));
        assertEquals(ErrorCodes.FEATURE_NOT_FOUND, ex.getCode());
    }

    @Test
    void enableFeature_createsNewConfig() {
        when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, Environment.DEV, "clientA"))
                .thenReturn(Optional.empty());

        FeatureActivationRequestDTO dto = FeatureActivationRequestDTO.builder()
                .environment(Environment.DEV)
                .clientId("clientA")
                .enabled(true)
                .build();

        String msg = service.enableFeatureForClientOrEnv(featureId, dto);

        assertTrue(msg.contains("activada correctamente"));
        verify(featureConfigRepository).save(any(FeatureConfig.class));
    }

    @Test
    void enableFeature_withDisabledFlag_throwsApiException() {
        when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));

        FeatureActivationRequestDTO dto = FeatureActivationRequestDTO.builder()
                .environment(Environment.DEV)
                .clientId("c1")
                .enabled(false)
                .build();

        ApiException ex = assertThrows(ApiException.class,
                () -> service.enableFeatureForClientOrEnv(featureId, dto));

        assertEquals(ErrorCodes.BAD_PARAMS, ex.getCode());
    }

    @Test
    void enableFeature_withNullEnabled_createsNewConfigAndForcesTrue() {
        when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, Environment.DEV, "clientB"))
                .thenReturn(Optional.empty());

        FeatureActivationRequestDTO dto = FeatureActivationRequestDTO.builder()
                .environment(Environment.DEV)
                .clientId("clientB")
                .enabled(null) 
                .build();

        String msg = service.enableFeatureForClientOrEnv(featureId, dto);

        assertTrue(msg.contains("activada correctamente"));
        verify(featureConfigRepository).save(any(FeatureConfig.class));
    }

    @Test
    void disableFeature_updatesConfig() {
        FeatureConfig config = FeatureConfig.builder()
                .feature(feature)
                .environment(Environment.PROD)
                .clientId("c1")
                .enabled(true)
                .build();

        when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, Environment.PROD, "c1"))
                .thenReturn(Optional.of(config));

        FeatureActivationRequestDTO dto = FeatureActivationRequestDTO.builder()
                .environment(Environment.PROD)
                .clientId("c1")
                .enabled(false)
                .build();

        String msg = service.disableFeatureForClientOrEnv(featureId, dto);

        assertTrue(msg.contains("desactivada correctamente"));
        assertFalse(config.isEnabled());
        verify(featureConfigRepository).save(config);
    }

    @Test
    void disableFeature_withEnabledFlag_throwsApiException() {
        FeatureConfig config = FeatureConfig.builder()
                .feature(feature)
                .environment(Environment.PROD)
                .clientId("c1")
                .enabled(true)
                .build();

        when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, Environment.PROD, "c1"))
                .thenReturn(Optional.of(config));

        FeatureActivationRequestDTO dto = FeatureActivationRequestDTO.builder()
                .environment(Environment.PROD)
                .clientId("c1")
                .enabled(true)
                .build();

        ApiException ex = assertThrows(ApiException.class,
                () -> service.disableFeatureForClientOrEnv(featureId, dto));

        assertEquals(ErrorCodes.BAD_PARAMS, ex.getCode());
    }

    @Test
    void disableFeature_configNotFound_throwsApiException() {
        when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, Environment.DEV, "cX"))
                .thenReturn(Optional.empty());

        FeatureActivationRequestDTO dto = FeatureActivationRequestDTO.builder()
                .environment(Environment.DEV)
                .clientId("cX")
                .enabled(false)
                .build();

        ApiException ex = assertThrows(ApiException.class,
                () -> service.disableFeatureForClientOrEnv(featureId, dto));

        assertEquals(ErrorCodes.BAD_PARAMS, ex.getCode());
    }

    @Test
    void isFeatureActived_configByClientPresent() {
        FeatureConfig config = FeatureConfig.builder()
                .feature(feature)
                .environment(Environment.DEV)
                .clientId("c1")
                .enabled(true)
                .build();

        when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, Environment.DEV, "c1"))
                .thenReturn(Optional.of(config));

        boolean active = service.isFeatureActived(featureId, "c1", Environment.DEV);

        assertTrue(active);
    }

    @Test
    void isFeatureActived_configByEnvPresent() {
        when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, Environment.DEV, "c1"))
                .thenReturn(Optional.empty());
        when(featureConfigRepository.findByFeatureIdAndEnvironmentAndClientIdIsNull(featureId, Environment.DEV))
                .thenReturn(Optional.of(FeatureConfig.builder().enabled(true).build()));

        assertTrue(service.isFeatureActived(featureId, "c1", Environment.DEV));
    }

    @Test
    void isFeatureActived_noConfig_usesEnabledByDefault() {
        feature.setEnabledByDefault(false);

        when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, Environment.STAGING, "c2"))
                .thenReturn(Optional.empty());
        when(featureConfigRepository.findByFeatureIdAndEnvironmentAndClientIdIsNull(featureId, Environment.STAGING))
                .thenReturn(Optional.empty());

        assertFalse(service.isFeatureActived(featureId, "c2", Environment.STAGING));
    }

    @Test
    void isFeatureActived_featureNotFound_throwsApiException() {
        when(featureRepository.findById(featureId)).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class,
                () -> service.isFeatureActived(featureId, "c1", Environment.DEV));

        assertEquals(ErrorCodes.FEATURE_NOT_FOUND, ex.getCode());
    }

}
