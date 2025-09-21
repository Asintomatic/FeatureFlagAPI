package com.bytescolab.featureflag.repository;

import com.bytescolab.featureflag.model.entity.Feature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FeatureRepositoryTest {

    @Autowired
    private FeatureRepository featureRepository;

    @Test
    void finders_work() {
        Feature f1 = Feature.builder().name("alpha").description("d").enabledByDefault(true).build();
        Feature f2 = Feature.builder().name("beta").description("d").enabledByDefault(false).build();
        Feature f3 = Feature.builder().name("BETAz").description("d").enabledByDefault(false).build();
        featureRepository.saveAll(List.of(f1, f2, f3));

        assertTrue(featureRepository.existsByName("alpha"));
        assertEquals(2, featureRepository.findByEnabledByDefault(false).size());
        assertEquals(2, featureRepository.findByNameContainingIgnoreCase("beta").size());
        assertEquals(2, featureRepository.findByEnabledByDefaultAndNameContainingIgnoreCase(false, "beta").size());
    }
}