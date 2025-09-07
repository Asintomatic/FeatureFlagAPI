package com.bytescolab.featureflag.service.feature;

import com.bytescolab.featureflag.dto.feature.FeatureDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDetailDTO;
import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.repository.FeatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class FeatureServiceImpl implements  FeatureService {

    private final FeatureRepository featureRepository;

    public FeatureServiceImpl(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Override
    public List<FeatureDTO> getAllFeatures() {
        return featureRepository.findAll()
                .stream()
                .map(feature -> new FeatureDTO(
                        feature.getId(),
                        feature.getName(),
                        feature.getDescription(),
                        feature.getEnabledByDefault()))
                .collect(toList());
    }

    @Override
    public FeatureDetailDTO getFeatureById(UUID id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found"));
        List<String> configNames = feature.getConfigs()
                .stream()
                .map(config -> config.getEnvironment() + ": " + config.getEnabled())
                .collect(Collectors.toList());

        return new FeatureDetailDTO(
                feature.getId(),
                feature.getName(),
                feature.getDescription(),
                feature.getEnabledByDefault(),
                configNames
        );
    }
}
