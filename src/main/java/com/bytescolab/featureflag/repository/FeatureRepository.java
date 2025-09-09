package com.bytescolab.featureflag.repository;

import com.bytescolab.featureflag.model.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeatureRepository extends JpaRepository <Feature, UUID> {
    boolean existsByName(String name);
}
