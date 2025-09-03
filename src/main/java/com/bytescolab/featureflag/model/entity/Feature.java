package com.bytescolab.featureflag.model.entity;

import java.util.List;
import java.util.UUID;

public class Feature {

    private UUID id;
    private String name;
    private String description;
    private Boolean enabledByDefault;
    private List<FeatureConfig> configs;

}
