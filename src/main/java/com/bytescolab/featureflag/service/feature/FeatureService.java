package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureSummaryResponseDTO;

import java.util.List;
import java.util.UUID;

public interface FeatureService {

    FeatureDetailResponseDTO createFeature(FeatureCreateRequestDTO dto);

    List<FeatureSummaryResponseDTO> getAllFeatures();

    FeatureDetailResponseDTO getFeatureById(UUID id);

    String enableFeatureForClientOrEnv(UUID featureId, FeatureActivationRequestDTO dto);

    String disableFeatureForClientOrEnv(UUID featureId, FeatureActivationRequestDTO dto);

    boolean isFeatureActived(UUID featureId, String clientId, String environment);
}
