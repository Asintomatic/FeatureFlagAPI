package com.bytescolab.featureflag.mapper;

import com.bytescolab.featureflag.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.dto.feature.request.FeatureConfigCreateRequestDTO;
import com.bytescolab.featureflag.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureConfigResponseDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.exception.ApiException;
import com.bytescolab.featureflag.exception.ErrorCodes;
import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;

import java.util.List;

/**
 * Utility class that provides mapping methods between
 * {@link Feature}, {@link FeatureConfig} entities and their corresponding DTOs.
 * <p>
 * This class is stateless and contains only static methods.
 * It cannot be instantiated.
 */
public class FeatureMapper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FeatureMapper() {
        throw new ApiException(ErrorCodes.BAD_REQUEST,ErrorCodes.BAD_REQUEST_MSG);
    }

    /**
     * Converts a {@link FeatureCreateRequestDTO} into a {@link Feature} entity.
     * If the request contains configuration DTOs, they are also mapped to {@link FeatureConfig}.
     *
     * @param dto the feature creation request DTO
     * @return a {@link Feature} entity populated with the DTO data
     */
    public static Feature toEntity(FeatureCreateRequestDTO dto) {
        Feature feature = Feature.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .enabledByDefault(dto.getEnabledByDefault())
                .build();

        if (dto.getConfigs() != null) {
            List<FeatureConfig> configs = dto.getConfigs().stream().map(feaConfigDto ->
                    FeatureConfig.builder()
                            .environment(feaConfigDto.getEnvironment())
                            .clientId(feaConfigDto.getClientId())
                            .enabled(feaConfigDto.getEnabled())
                            .feature(feature)
                            .build()
            ).toList();

            feature.setConfigs(configs);
        }

        return feature;
    }

    /**
     * Converts a {@link Feature} entity into a {@link FeatureDetailResponseDTO}.
     * Includes feature configurations if present.
     *
     * @param feature the feature entity
     * @return a {@link FeatureDetailResponseDTO} representation of the feature
     */
    public static FeatureDetailResponseDTO toDetailResponseDTO(Feature feature) {
        return FeatureDetailResponseDTO.builder()
                .id(feature.getId())
                .name(feature.getName())
                .description(feature.getDescription())
                .enabledByDefault(feature.getEnabledByDefault())
                .configs(feature.getConfigs() != null ? feature.getConfigs().stream()
                        .map(FeatureMapper::toConfigDTO)
                        .toList() : null)
                .build();
    }

    /**
     * Converts a {@link Feature} entity into a {@link FeatureSummaryResponseDTO}.
     * Provides only basic summary information.
     *
     * @param feature the feature entity
     * @return a summary DTO with id, name, and description
     */
    public static FeatureSummaryResponseDTO toSummaryDTO(Feature feature) {
        return FeatureSummaryResponseDTO.builder()
                .id(feature.getId())
                .name(feature.getName())
                .description(feature.getDescription())
                .build();
    }

    /**
     * Converts a {@link FeatureConfig} entity into a {@link FeatureConfigResponseDTO}.
     *
     * @param config the feature configuration entity
     * @return a DTO representation of the feature configuration
     */
    public static FeatureConfigResponseDTO toConfigDTO(FeatureConfig config) {
        return FeatureConfigResponseDTO.builder()
                .id(config.getId())
                .environment(config.getEnvironment())
                .clientId(config.getClientId())
                .enabled(config.getEnabled())
                .build();
    }

    /**
     * Converts a {@link FeatureConfigCreateRequestDTO} into a {@link FeatureConfig} entity.
     *
     * @param dto     the config creation request DTO
     * @param feature the feature associated with the configuration
     * @return a {@link FeatureConfig} entity
     */
    public static FeatureConfig toConfigEntity(FeatureConfigCreateRequestDTO dto, Feature feature) {
        return FeatureConfig.builder()
                .environment(dto.getEnvironment())
                .clientId(dto.getClientId())
                .enabled(dto.getEnabled())
                .feature(feature)
                .build();
    }

    /**
     * Converts a {@link FeatureActivationRequestDTO} into a {@link FeatureConfig} entity.
     * If {@code enabled} is null in the request, defaults to {@code true}.
     *
     * @param dto     the feature activation request DTO
     * @param feature the feature associated with the configuration
     * @return a {@link FeatureConfig} entity
     */
    public static FeatureConfig toConfigEntity(FeatureActivationRequestDTO dto, Feature feature) {
        return FeatureConfig.builder()
                .environment(dto.getEnvironment())
                .clientId(dto.getClientId())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE)
                .feature(feature)
                .build();
    }
}
