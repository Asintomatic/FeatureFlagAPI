package com.bytescolab.featureflag.dto.feature;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureConfigDTO {

    private UUID id;

    @NotBlank(message = "environment is required")
    private String environment;

    private String clientId;

    @NotNull(message = "enabled is necessary")
    private Boolean enabled;

}
