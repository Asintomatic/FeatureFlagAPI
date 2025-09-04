package com.bytescolab.featureflag.model.entity;

import com.bytescolab.featureflag.model.enums.Environment;

import java.util.UUID;

public class FeatureConfig {

    private UUID id;
    private Environment environment;
    private String clientId;
    private Boolean enabled;
    private Feature feature;

}
