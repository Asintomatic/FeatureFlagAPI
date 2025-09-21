package com.bytescolab.featureflag.repository;

import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;
import com.bytescolab.featureflag.model.enums.Environment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FeatureConfigRepositoryTest {

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private FeatureConfigRepository configRepository;

    @Test
    void finders_work() {
        Feature feature = featureRepository.save(Feature.builder()
                .name("flag-1").description("d").enabledByDefault(true).build());

        FeatureConfig config = FeatureConfig.builder()
                .feature(feature)
                .environment(Environment.DEV)
                .clientId("c1")
                .enabled(true)
                .build();
        configRepository.save(config);

        Optional<FeatureConfig> byClient = configRepository.findByFeatureAndEnvironmentAndClientId(feature, Environment.DEV, "c1");
        assertTrue(byClient.isPresent());
        assertTrue(configRepository.existsByFeatureAndEnvironmentAndClientId(feature, Environment.DEV, "c1"));

        assertTrue(configRepository.findByFeatureIdAndEnvironmentAndClientIdIsNull(feature.getId(), Environment.DEV).isEmpty());
    }
}