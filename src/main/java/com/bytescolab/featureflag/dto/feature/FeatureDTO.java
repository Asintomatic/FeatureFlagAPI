package com.bytescolab.featureflag.dto.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureDTO {

    private UUID id;

    private String name;

    private String description;

    private Boolean enabled;

}
