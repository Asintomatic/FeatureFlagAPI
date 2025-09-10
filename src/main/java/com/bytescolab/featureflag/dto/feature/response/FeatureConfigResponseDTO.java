package com.bytescolab.featureflag.dto.feature.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureConfigResponseDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    private String environment;

    private String clientId;

    private Boolean enabled;
}
