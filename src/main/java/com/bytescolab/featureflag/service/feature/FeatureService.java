package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.dto.feature.FeatureDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDetailDTO;
import com.bytescolab.featureflag.dto.feature.FeatureEnableDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public interface FeatureService {

    List<FeatureDTO> getAllFeatures();

    FeatureDetailDTO getFeatureById(UUID id);

    void enableFeatureForClientOrEnv(UUID featureId, String clientId, String environment);

    void disableFeatureForClientOrEnv(UUID featureId, String clientId, String environment);
}
