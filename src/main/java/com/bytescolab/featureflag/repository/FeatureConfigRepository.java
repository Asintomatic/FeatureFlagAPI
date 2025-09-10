package com.bytescolab.featureflag.repository;

import com.bytescolab.featureflag.model.entity.Feature;
import com.bytescolab.featureflag.model.entity.FeatureConfig;
import com.bytescolab.featureflag.model.enums.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeatureConfigRepository extends JpaRepository <FeatureConfig, UUID> {

    Optional<FeatureConfig> findByFeatureAndEnvironmentAndClientId(Feature feature, Environment environment, String clientId);

    boolean existsByFeatureAndEnvironmentAndClientId(Feature feature, Environment environment, String clientId);

    Optional<FeatureConfig> findByFeatureIdAndEnvironmentAndClientIdIsNull(UUID featureId, Environment env);

}