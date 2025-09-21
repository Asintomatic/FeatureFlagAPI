package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.exception.ApiException;
import com.bytescolab.featureflag.exception.ErrorCodes;
import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;
import com.bytescolab.featureflag.model.enums.Environment;
import com.bytescolab.featureflag.repository.FeatureConfigRepository;
import com.bytescolab.featureflag.repository.FeatureRepository;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureConfigCreateRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureConfigResponseDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.repository.mapper.FeatureMapper;
import com.bytescolab.featureflag.config.security.SecurityUtils;
import com.bytescolab.featureflag.utils.logging.FeatureAuditLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;
    private final FeatureConfigRepository featureConfigRepository;
    private final FeatureAuditLogger featureAuditLogger;


    public FeatureServiceImpl(FeatureRepository featureRepository,
                              FeatureConfigRepository featureConfigRepository, FeatureAuditLogger featureAuditLogger) {
        this.featureRepository = featureRepository;
        this.featureConfigRepository = featureConfigRepository;
        this.featureAuditLogger = featureAuditLogger;
    }

    @Override
    public FeatureDetailResponseDTO createFeature(FeatureCreateRequestDTO dto) {
        if (featureRepository.existsByName(dto.getName())) {
            throw new ApiException(ErrorCodes.FEATURE_EXISTS, ErrorCodes.FEATURE_EXISTS_MSG);
        }

        Feature entity = FeatureMapper.toEntity(dto);
        Feature saved = featureRepository.save(entity);
        String currentUser = SecurityUtils.getCurrentUser();

        featureAuditLogger.logCreation(dto.getName(), currentUser);
        log.info("Feature {} creada con éxito.", entity.getName());

        return FeatureMapper.toDetailResponseDTO(saved);
    }

    @Override
    public FeatureConfigResponseDTO createConfigFeature(UUID id, FeatureConfigCreateRequestDTO dto) {

        Feature feature = getFeatureOrThrow(id);
        boolean exists = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, dto.getEnvironment(), dto.getClientId())
                .isPresent();

        if (exists) {
            throw new ApiException(ErrorCodes.FEATURE_EXISTS_CONFIG,ErrorCodes.FEATURE_EXISTS_CONFIG_MSG);
        }

        FeatureConfig config = FeatureConfig.builder()
                .feature(feature)
                .environment(dto.getEnvironment())
                .clientId(dto.getClientId())
                .enabled(dto.getEnabled())
                .build();

        FeatureConfig saved = featureConfigRepository.save(config);

        return FeatureConfigResponseDTO.builder()
                .environment(saved.getEnvironment())
                .clientId(saved.getClientId())
                .enabled(saved.isEnabled())
                .build();
    }


    @Override
    public List<FeatureSummaryResponseDTO> getAllFeatures(Boolean enabled, String name) {
        var features = featureRepository.findAll();

        return features.stream()
                .filter(feature -> name == null || feature.getName().equalsIgnoreCase(name))
                .filter(feature -> {
                    if (enabled != null) {
                        return feature.getConfigs().stream()
                                .anyMatch(config -> config.isEnabled() == enabled);
                    } else {

                        return feature.isEnabledByDefault();
                    }
                })
                .map(FeatureMapper::toSummaryDTO)
                .toList();
    }

    @Override
    public FeatureDetailResponseDTO getFeatureById(UUID id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCodes.FEATURE_NOT_FOUND, ErrorCodes.FEATURE_NOT_FOUND_MSG));
        return FeatureMapper.toDetailResponseDTO(feature);
    }

    @Override
    public String enableFeatureForClientOrEnv(UUID featureId, FeatureActivationRequestDTO dto) {
        boolean isActivated = isFeatureActived(featureId, dto.getClientId(), dto.getEnvironment());
        if (isActivated) {
            throw new ApiException(ErrorCodes.FEATURE_ENABLE, ErrorCodes.FEATURE_ENABLE_MSG);
        }
        if (Boolean.FALSE.equals(dto.getEnabled())) {
            throw new ApiException(ErrorCodes.BAD_PARAMS, ErrorCodes.BAD_PARAMS_MSG);
        }
        Feature feature = getFeatureOrThrow(featureId);

        FeatureConfig config = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, dto.getEnvironment(), dto.getClientId())
                .orElse(FeatureMapper.toConfigEntity(dto, feature));

        String currentUser = SecurityUtils.getCurrentUser();
        config.setEnabled(true);
        featureConfigRepository.save(config);

        featureAuditLogger.logActivation(feature.getName(), dto.getEnvironment().toString(), dto.getClientId(), currentUser);
        log.info("Feature '{}' activada correctamente para clientId: '{}' y env: '{}'",
                feature.getName(), dto.getClientId(), dto.getEnvironment());

        return String.format("Feature '%s' activada correctamente para clientId= %s y env= %s",
                feature.getName(), dto.getClientId(), dto.getEnvironment());
    }

    @Override
    public String disableFeatureForClientOrEnv(UUID featureId, FeatureActivationRequestDTO dto) {
        boolean isActivated = isFeatureActived(featureId, dto.getClientId(), dto.getEnvironment());
        if (!isActivated) {
            throw new ApiException(ErrorCodes.FEATURE_DISABLE, ErrorCodes.FEATURE_DISABLE_MSG);
        }
        if (Boolean.TRUE.equals(dto.getEnabled())) {
            throw new ApiException(ErrorCodes.BAD_PARAMS, ErrorCodes.BAD_PARAMS_MSG);
        }
        Feature feature = getFeatureOrThrow(featureId);

        FeatureConfig config = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, dto.getEnvironment(), dto.getClientId())
                .orElseThrow(() -> new ApiException(ErrorCodes.BAD_PARAMS, "No existe configuración para deshabilitar"));

        String currentUser = SecurityUtils.getCurrentUser();
        config.setEnabled(false);
        featureConfigRepository.save(config);

        featureAuditLogger.logDesactivation(feature.getName(), dto.getEnvironment().toString(), dto.getClientId(), currentUser);
        log.info("Feature '{}' desactivada correctamente para clientId: '{}' y env: '{}'",
                feature.getName(), dto.getClientId(), dto.getEnvironment());

        return String.format("Feature '%s' desactivada correctamente para clientId= %s y env= %s",
                feature.getName(), dto.getClientId(), dto.getEnvironment());
    }

    @Override
    public boolean isFeatureActived(UUID featureId, String clientId, Environment environment) {
        Feature feature = getFeatureOrThrow(featureId);

        return featureConfigRepository.findByFeatureAndEnvironmentAndClientId(feature, environment, clientId)
                .map(FeatureConfig::isEnabled)
                .or(() -> featureConfigRepository.findByFeatureIdAndEnvironmentAndClientIdIsNull(feature.getId(), environment)
                        .map(FeatureConfig::isEnabled))
                .orElse(feature.isEnabledByDefault());
    }

    private Feature getFeatureOrThrow(UUID featureId) {
        return featureRepository.findById(featureId)
                .orElseThrow(() -> new ApiException(ErrorCodes.FEATURE_NOT_FOUND, ErrorCodes.FEATURE_NOT_FOUND_MSG));
    }
}