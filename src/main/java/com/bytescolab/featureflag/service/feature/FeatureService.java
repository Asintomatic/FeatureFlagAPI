package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.repository.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.model.enums.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface FeatureService {

    FeatureDetailResponseDTO createFeature(FeatureCreateRequestDTO dto);

    List<FeatureSummaryResponseDTO> getAllFeatures(Boolean enabled, String name);

    FeatureDetailResponseDTO getFeatureById(UUID id);

    String enableFeatureForClientOrEnv(UUID featureId, FeatureActivationRequestDTO dto);

    String disableFeatureForClientOrEnv(UUID featureId, FeatureActivationRequestDTO dto);

    boolean isFeatureActived(UUID featureId, String clientId, Environment environment);
}
