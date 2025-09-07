package com.bytescolab.featureflag.repository;

import com.bytescolab.featureflag.model.entity.FeatureConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeatureConfigRepository extends JpaRepository <FeatureConfig, UUID> {
}
