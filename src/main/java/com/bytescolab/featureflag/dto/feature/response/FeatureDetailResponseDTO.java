package com.bytescolab.featureflag.dto.feature.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureDetailResponseDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    private String name;

    private String description;

    private Boolean enabledByDefault;

    private List<FeatureConfigResponseDTO> configs;
}
