package com.bytescolab.featureflag.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "features")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "configs")
public class Feature {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    private Boolean enabledByDefault;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL)
    private List<FeatureConfig> configs;

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

}
