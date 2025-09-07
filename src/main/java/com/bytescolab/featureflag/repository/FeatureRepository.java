package com.bytescolab.featureflag.repository;

import com.bytescolab.featureflag.model.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeatureRepository extends JpaRepository <Feature, UUID> {
}
