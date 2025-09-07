package com.bytescolab.featureflag.dto.feature;

import lombok.Data;

@Data
public class FeatureEnableDTO {

    private String environment;
    private String clientId;
}
