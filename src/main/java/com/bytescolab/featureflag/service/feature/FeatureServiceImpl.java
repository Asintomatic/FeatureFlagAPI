package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.exception.ApiException;
import com.bytescolab.featureflag.exception.ErrorCodes;
import com.bytescolab.featureflag.mapper.FeatureMapper;
import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;
import com.bytescolab.featureflag.model.enums.Environment;
import com.bytescolab.featureflag.repository.FeatureConfigRepository;
import com.bytescolab.featureflag.repository.FeatureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
            throw new ApiException(ErrorCodes.FEATURE_EXISTS, ErrorCodes.FEATURE_EXISTS_MSG);
        }

        Feature entity = FeatureMapper.toEntity(dto);
        Feature saved = featureRepository.save(entity);
        log.info("Feature {} creada con éxito.", entity.getName());

        return FeatureMapper.toDetailResponseDTO(saved);
    }

    @Override
    public List<FeatureSummaryResponseDTO> getAllFeatures(Boolean enabled, String name) {
        if (enabled != null && name != null) {
            validateFeatureExists(name);
            return featureRepository.findByEnabledByDefaultAndNameContainingIgnoreCase(enabled, name)
                    .stream().map(FeatureMapper::toSummaryDTO).toList();
        }
        if (name != null) {
            validateFeatureExists(name);
            return featureRepository.findByNameContainingIgnoreCase(name)
                    .stream().map(FeatureMapper::toSummaryDTO).toList();
        }
        if (enabled != null) {
            return featureRepository.findByEnabledByDefault(enabled)
                    .stream().map(FeatureMapper::toSummaryDTO).toList();
        }
        return featureRepository.findAll().stream()
                .map(FeatureMapper::toSummaryDTO).toList();
    }

    @Override
    public FeatureDetailResponseDTO getFeatureById(UUID id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCodes.FEATURE_NOT_FOUND, ErrorCodes.FEATURE_NOT_FOUND_MSG));
        return FeatureMapper.toDetailResponseDTO(feature);
    }

    @Override
    public String enableFeatureForClientOrEnv(UUID featureId, FeatureActivationRequestDTO dto) {
        if (Boolean.FALSE.equals(dto.getEnabled())) {
            throw new ApiException(ErrorCodes.BAD_PARAMS, ErrorCodes.BAD_PARAMS_MSG);
        }
        Feature feature = getFeatureOrThrow(featureId);

        FeatureConfig config = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, dto.getEnvironment(), dto.getClientId())
                .orElse(FeatureMapper.toConfigEntity(dto, feature));

        config.setEnabled(true);
        featureConfigRepository.save(config);
        log.info("Feature '{}' activada correctamente para clientId: '{}' y env: '{}'",
                feature.getName(), dto.getClientId(), dto.getEnvironment());

        return String.format("Feature '%s' activada correctamente para clientId=%s y env=%s",
                feature.getName(), dto.getClientId(), dto.getEnvironment());
    }

    @Override
    public String disableFeatureForClientOrEnv(UUID featureId, FeatureActivationRequestDTO dto) {
        if (Boolean.TRUE.equals(dto.getEnabled())) {
            throw new ApiException(ErrorCodes.BAD_PARAMS, ErrorCodes.BAD_PARAMS_MSG);
        }
        Feature feature = getFeatureOrThrow(featureId);

        FeatureConfig config = featureConfigRepository
                .findByFeatureAndEnvironmentAndClientId(feature, dto.getEnvironment(), dto.getClientId())
                .orElseThrow(() -> new ApiException(ErrorCodes.BAD_PARAMS, "No existe configuración para deshabilitar"));

        config.setEnabled(false);
        featureConfigRepository.save(config);
        log.info("Feature '{}' desactivada correctamente para clientId: '{}' y env: '{}'",
                feature.getName(), dto.getClientId(), dto.getEnvironment());

        return String.format("Feature '%s' desactivada correctamente para clientId=%s y env=%s",
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

    private void validateFeatureExists(String name) {
        if (!featureRepository.existsByName(name)) {
            throw new ApiException(ErrorCodes.FEATURE_NOT_FOUND, "No hay ninguna feature con ese nombre");
        }
    }

    private Feature getFeatureOrThrow(UUID featureId) {
        return featureRepository.findById(featureId)
                .orElseThrow(() -> new ApiException(ErrorCodes.FEATURE_NOT_FOUND, ErrorCodes.FEATURE_NOT_FOUND_MSG));
    }
}