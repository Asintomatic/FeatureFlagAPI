package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.dto.feature.FeatureDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements  FeatureService{
    @Override
    public List<FeatureDTO> getAllFeatures() {
        return List.of();
    }

    @Override
    public FeatureDetailDTO getFeatureById(UUID id) {
        return null;
    }
}
