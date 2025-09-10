package com.bytescolab.featureflag.dto.feature.response;

import com.bytescolab.featureflag.model.enums.Environment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureConfigResponseDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    private Environment environment;

    private String clientId;

    private Boolean enabled;
}
