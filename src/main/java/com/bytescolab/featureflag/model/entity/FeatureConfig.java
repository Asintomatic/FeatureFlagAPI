package com.bytescolab.featureflag.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Table(name = "feature_configs", uniqueConstraints = {@UniqueConstraint(columnNames = {"feature_flag_id", "environment", "client_id"})})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "feature")
public class FeatureConfig {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

    @Column(name = "environment", nullable = false)
    private String environment;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(nullable = false)
    private Boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

}
