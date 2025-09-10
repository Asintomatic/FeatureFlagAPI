package com.bytescolab.featureflag.dto.feature.request;

import com.bytescolab.featureflag.model.enums.Environment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureActivationRequestDTO {

    @NotNull(message = "Environment es necesario")
    private Environment environment;

    @NotBlank (message = "clientId es necesario")
    private String clientId;

    private Boolean enabled;
}
