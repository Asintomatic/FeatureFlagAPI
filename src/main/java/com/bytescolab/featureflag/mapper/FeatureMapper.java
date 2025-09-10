package com.bytescolab.featureflag.mapper;

import com.bytescolab.featureflag.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.dto.feature.request.FeatureConfigCreateRequestDTO;
import com.bytescolab.featureflag.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureConfigResponseDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;

import java.util.List;
import java.util.stream.Collectors;



public class FeatureMapper {

    public static Feature toEntity(FeatureCreateRequestDTO dto) {
        Feature feature = Feature.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .enabledByDefault(dto.getEnabledByDefault())
                .build();

        if (dto.getConfigs() != null) {
            List<FeatureConfig> configs = dto.getConfigs().stream().map(cfgDto ->
                FeatureConfig.builder()
                    .environment(cfgDto.getEnvironment())
                    .clientId(cfgDto.getClientId())
                    .enabled(cfgDto.getEnabled())
                    .feature(feature)
                    .build()
            ).collect(Collectors.toList());

            feature.setConfigs(configs);
        }

        return feature;
    }

    public static FeatureDetailResponseDTO toDetailResponseDTO(Feature feature) {
        return FeatureDetailResponseDTO.builder()
                .id(feature.getId())
                .name(feature.getName())
                .description(feature.getDescription())
                .enabledByDefault(feature.getEnabledByDefault())
                .configs(feature.getConfigs() != null ? feature.getConfigs().stream()
                        .map(FeatureMapper::toConfigDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public static FeatureSummaryResponseDTO toSummaryDTO(Feature feature) {
        return FeatureSummaryResponseDTO.builder()
                .id(feature.getId())
                .name(feature.getName())
                .description(feature.getDescription())
                .build();
    }

    public static FeatureConfigResponseDTO toConfigDTO(FeatureConfig config) {
        return FeatureConfigResponseDTO.builder()
                .id(config.getId())
                .environment(config.getEnvironment())
                .clientId(config.getClientId())
                .enabled(config.getEnabled())
                .build();
    }

    public static FeatureConfig toConfigEntity(FeatureConfigCreateRequestDTO dto, Feature feature) {
        return FeatureConfig.builder()
                .environment(dto.getEnvironment())
                .clientId(dto.getClientId())
                .enabled(dto.getEnabled())
                .feature(feature)
                .build();
    }
    public static FeatureConfig toConfigEntity(FeatureActivationRequestDTO dto, Feature feature) {
        return FeatureConfig.builder()
                .environment(dto.getEnvironment())
                .clientId(dto.getClientId())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE)
                .feature(feature)
                .build();
    }
}
