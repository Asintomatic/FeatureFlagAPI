package com.bytescolab.featureflag.dto.feature.response;

import lombok.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureSummaryResponseDTO {

    private UUID id;

    private String name;

    private String description;

}
