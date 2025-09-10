package com.bytescolab.featureflag.dto.feature.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureActivationRequestDTO {

    @NotBlank (message = "Environment es necesario")
    private String environment;

    @NotBlank (message = "clientId es necesario")
    private String clientId;

    private Boolean enabled;
}
