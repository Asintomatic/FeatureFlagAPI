package com.bytescolab.featureflag.dto.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDetailDTO {

    private UUID id;

    private String name;

    private String description;

    private Boolean enabled;

    private List<String> configs;
}
