package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.dto.feature.FeatureDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDetailDTO;

import java.util.List;
import java.util.UUID;

public interface FeatureService {

    List<FeatureDTO> getAllFeatures();

    FeatureDetailDTO getFeatureById(UUID id);
}
