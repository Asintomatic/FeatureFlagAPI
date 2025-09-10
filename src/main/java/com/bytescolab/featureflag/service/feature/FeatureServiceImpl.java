package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.mapper.FeatureMapper;
import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;
import com.bytescolab.featureflag.model.enums.Environment;
import com.bytescolab.featureflag.repository.FeatureConfigRepository;
import com.bytescolab.featureflag.repository.FeatureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;
    private final FeatureConfigRepository featureConfigRepository;

    public FeatureServiceImpl(FeatureRepository featureRepository,
                              FeatureConfigRepository featureConfigRepository) {
        this.featureRepository = featureRepository;
        this.featureConfigRepository = featureConfigRepository;
    }

    @Override
    public FeatureDetailResponseDTO createFeature(FeatureCreateRequestDTO dto) {
        if (featureRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Feature ya existe");
        }

        Feature entity = FeatureMapper.toEntity(dto);
        Feature saved = featureRepository.save(entity);

        return FeatureMapper.toDetailResponseDTO(saved);
    }

    @Override
    public List<FeatureSummaryResponseDTO> getAllFeatures(Boolean enabled, String name) {
        List<Feature> features;
        boolean feature;

        if (enabled != null && name != null) {
            feature = featureRepository.existsByName(name);
            if(feature){
                features = featureRepository.findByEnabledByDefaultAndNameContainingIgnoreCase(enabled, name);
            }else {
                throw new IllegalArgumentException(" No hay ninguna feature con ese nombre");
            }
        } else if (name != null) {
            feature = featureRepository.existsByName(name);
            if(feature){
                features = featureRepository.findByNameContainingIgnoreCase(name);
            }else {
                throw new IllegalArgumentException(" No hay ninguna feature con ese nombre");
            }
        } else if (enabled != null) {
            features = featureRepository.findByEnabledByDefault(enabled);
        } else {
            features = featureRepository.findAll();
        }

        return features.stream()
                .map(FeatureMapper::toSummaryDTO)
                .toList();
    }

    @Override
    public FeatureDetailResponseDTO getFeatureById(UUID id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature no encontrada"));

        return FeatureMapper.toDetailResponseDTO(feature);
    }

    @Override
    public String enableFeatureForClientOrEnv(UUID featureId, FeatureActivationRequestDTO dto) {
        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature no encontrada"));

        FeatureConfig config = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, dto.getEnvironment(), dto.getClientId())
                .orElse(null);

        if (config == null) {
            config = FeatureMapper.toConfigEntity(dto, feature);
            log.info("Creando nueva configuración para feature '{}'", feature.getName());
        } else {
            config.setEnabled(true);
            log.info("Actualizando configuración existente para feature '{}'", feature.getName());
        }

        featureConfigRepository.save(config);

        return String.format("Feature '%s' activada correctamente para clientId=%s y env=%s",
                feature.getName(), dto.getClientId(), dto.getEnvironment());
    }

    @Override
    public String disableFeatureForClientOrEnv(UUID featureId, FeatureActivationRequestDTO dto) {
        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature no encontrada"));

        FeatureConfig config = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, dto.getEnvironment(), dto.getClientId())
                .orElseThrow(() -> new RuntimeException("No existe asignación para deshabilitar"));

        config.setEnabled(false);
        featureConfigRepository.save(config);

        return String.format("Feature '%s' desactivada correctamente para clientId=%s y env=%s",
                feature.getName(), dto.getClientId(), dto.getEnvironment());
    }

    @Override
    public boolean isFeatureActived(UUID featureId, String clientId, Environment environment) {
        log.info("Buscando feature con ID: {}", featureId);
        Optional<Feature> featureOpt = featureRepository.findById(featureId);

        if (featureOpt.isEmpty()) {
            log.warn("Feature con ID {} no encontrada", featureId);
            return false;
        }

        Feature feature = featureOpt.get();

        Optional<FeatureConfig> configByClient = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, environment, clientId);

        if (configByClient.isPresent()) {
            return configByClient.get().isEnabled();
        }

        Optional<FeatureConfig> configByEnv = featureConfigRepository
                .findByFeatureIdAndEnvironmentAndClientIdIsNull(feature.getId(), environment);

        return configByEnv.map(FeatureConfig::isEnabled).orElseGet(feature::isEnabledByDefault);
    }
}
