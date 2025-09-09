package com.bytescolab.featureflag.dto.feature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureDetailDTO {

    private UUID id;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 50, message = "name must be 3-50 characters")
    private String name;

    @NotBlank(message = "description is required")
    @Size(min = 3, max = 50, message = "description must be 3-50 characters")
    private String description;

    @NotNull(message = "enabledByDefault is necessary")
    private Boolean enabledByDefault;

    private List<FeatureConfigDTO> configs;
}
