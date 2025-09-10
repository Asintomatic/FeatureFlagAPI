package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.dto.feature.FeatureDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDetailDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public interface FeatureService {

    FeatureDetailDTO createFeature(FeatureDetailDTO featureDetailDTO);

    List<FeatureDTO> getAllFeatures(Boolean enabled, String name);

    FeatureDetailDTO getFeatureById(UUID id);

    String enableFeatureForClientOrEnv(UUID featureId, String clientId, String environment);

    String disableFeatureForClientOrEnv(UUID featureId, String clientId, String environment);

    boolean isFeatureActived(UUID featureId, String clientId, String environment);

}
