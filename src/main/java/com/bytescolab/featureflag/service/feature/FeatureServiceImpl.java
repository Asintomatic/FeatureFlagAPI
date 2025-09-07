package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.dto.feature.FeatureDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDetailDTO;
import com.bytescolab.featureflag.dto.feature.FeatureEnableDTO;
import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;
import com.bytescolab.featureflag.repository.FeatureConfigRepository;
import com.bytescolab.featureflag.repository.FeatureRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
    public List<FeatureDTO> getAllFeatures() {
        return featureRepository.findAll()
                .stream()
                .map(feature -> new FeatureDTO(
                        feature.getId(),
                        feature.getName(),
                        feature.getDescription(),
                        feature.getEnabledByDefault()))
                .collect(toList());
    }

    @Override
    public FeatureDetailDTO getFeatureById(UUID id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found"));
        List<String> configNames = feature.getConfigs()
                .stream()
                .map(config -> config.getEnvironment() + ": " + config.getEnabled())
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
    public void enableFeatureForClientOrEnv(UUID featureId, String environment, String cliendId) {
        Feature feature = featureRepository.findById((featureId)).orElseThrow(() -> new RuntimeException("Feature no encontrada"));

        if(featureConfigRepository.existsByFeatureAndEnvironmentAndClientId(feature, environment, cliendId)) {
            FeatureConfig config = featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, environment, cliendId).get();;
            config.setEnabled(true);
            featureConfigRepository.save(config);

        }else {
            FeatureConfig newConfig = FeatureConfig.builder()
                    .feature(feature)
                    .environment(environment)
                    .clientId(cliendId)
                    .enabled(true)
                    .build();
            featureConfigRepository.save(newConfig);
        }
    }

    @Override
    public void  disableFeatureForClientOrEnv(UUID featureId, String environment, String cliendId) {
        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature no encontrada"));

        FeatureConfig assignment = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, environment, cliendId)
                .orElseThrow(() -> new RuntimeException("No existe asignación para deshabilitar"));

        assignment.setEnabled(false);
        featureConfigRepository.save(assignment);
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
