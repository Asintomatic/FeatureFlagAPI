package com.bytescolab.featureflag.model.entity;

import com.bytescolab.featureflag.model.enums.Environment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "feature_configs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureConfig {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Environment environment;


    private String clientId;


    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

}
