package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.dto.feature.FeatureConfigDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDetailDTO;
import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;
import com.bytescolab.featureflag.repository.FeatureConfigRepository;
import com.bytescolab.featureflag.repository.FeatureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FeatureServiceImpl implements  FeatureService {

    private final FeatureRepository featureRepository;
    private final FeatureConfigRepository featureConfigRepository;

    public FeatureServiceImpl(FeatureRepository featureRepository,
                              FeatureConfigRepository featureConfigRepository) {
        this.featureRepository = featureRepository;
        this.featureConfigRepository = featureConfigRepository;
    }

    @Override
    public FeatureDetailDTO createFeature(FeatureDetailDTO featureDetailDTO) {
        if(featureRepository.existsByName(featureDetailDTO.getName())) {
            throw new IllegalArgumentException("Feature name already exists");
        }

        List<FeatureConfig> lstFeatureConfigEntity = new ArrayList<>();

        if (featureDetailDTO.getConfigs() != null && !featureDetailDTO.getConfigs().isEmpty()) {
            FeatureConfig featureConfigEntity = FeatureConfig.builder()
                    .environment(featureDetailDTO.getConfigs().get(0).getEnvironment())
                    .clientId(featureDetailDTO.getConfigs().get(0).getClientId())
                    .enabled(featureDetailDTO.getConfigs().get(0).getEnabled())
                    .build();
            lstFeatureConfigEntity.add(featureConfigEntity);
        }

        Feature featureEntity = Feature.builder()
                .name(featureDetailDTO.getName())
                .description(featureDetailDTO.getDescription())
                .enabledByDefault(featureDetailDTO.getEnabledByDefault() == null ? Boolean.FALSE : featureDetailDTO.getEnabledByDefault())
                .configs(lstFeatureConfigEntity)
                .build();

        lstFeatureConfigEntity.get(0).setFeature(featureEntity);

        Feature featureSaved = featureRepository.save(featureEntity);

        return FeatureDetailDTO.builder()
                .id(featureSaved.getId())
                .name(featureSaved.getName())
                .description(featureSaved.getDescription())
                .enabledByDefault(featureSaved.getEnabledByDefault())
                .build();
    }

    @Override
    public List<FeatureDTO> getAllFeatures() {
        return featureRepository.findAll()
                .stream()
                .map(feature -> new FeatureDTO(
                        feature.getId(),
                        feature.getName(),
                        feature.getDescription(),
                        feature.getEnabledByDefault()))
                .toList();
    }

    @Override
    public FeatureDetailDTO getFeatureById(UUID id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found"));
        List<FeatureConfigDTO> configNames = feature.getConfigs()
                .stream()
                .map(config -> FeatureConfigDTO.builder()
                        .id(config.getId())
                        .environment(config.getEnvironment())
                        .enabled(config.getEnabled())
                        .clientId(config.getClientId())
                        .build())
                .collect(Collectors.toList());

        return new FeatureDetailDTO(
                feature.getId(),
                feature.getName(),
                feature.getDescription(),
                feature.getEnabledByDefault(),
                configNames
        );
    }

    @Override
    public String enableFeatureForClientOrEnv(UUID featureId, String environment, String clientId) {
        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature no encontrada"));

        FeatureConfig assignment = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, environment, clientId)
                .orElseThrow(() -> new RuntimeException("No existe asignación para deshabilitar"));

        assignment.setEnabled(false);
        featureConfigRepository.save(assignment);

        String msg = String.format("Feature '%s' activada correctamente para clientId=%s y env=%s",
                feature.getName(), clientId, environment);
        log.info("Feature '{}' activada correctamente para clientId={} y env={}",
                feature.getName(), clientId, environment);
        return msg;
    }

    @Override
    public String disableFeatureForClientOrEnv(UUID featureId, String clientId, String environment) {
        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature no encontrada"));

        FeatureConfig assignment = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, clientId, environment)
                .orElseThrow(() -> new RuntimeException("No existe asignación para deshabilitar"));

        assignment.setEnabled(false);
        featureConfigRepository.save(assignment);

        String msg = String.format("Feature '%s' desactivada correctamente para clientId=%s y env=%s",
                feature.getName(), clientId, environment);
        log.info("Feature '{}' desactivada correctamente para clientId={} y env={}",
                feature.getName(), clientId, environment);

        return msg;
    }

    @Override
    public boolean isFeatureActived(UUID featureId, String clientId, String environment) {

        log.info("Buscando feature con ID: {}", featureId);
        Optional<Feature> featureOpt = featureRepository.findById(featureId);

        if (featureOpt.isEmpty()) {
            log.warn("Feature con ID {} no encontrada", featureId);
            return false;
        }

        Feature feature = featureOpt.get();

        Optional<FeatureConfig> configByClient = featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, environment, clientId);
        log.info("Configuración por cliente: {}", configByClient);

        if (configByClient.isPresent()) {
            return configByClient.get().isEnabled();
        }

        Optional<FeatureConfig> configByEnv = featureConfigRepository.findByFeatureIdAndEnvironmentAndClientIdIsNull(feature.getId(), environment);
        log.info("Configuración por entorno: {}", configByEnv);

        return configByEnv.map(FeatureConfig::isEnabled).orElseGet(feature::isEnabledByDefault);


    }

}
