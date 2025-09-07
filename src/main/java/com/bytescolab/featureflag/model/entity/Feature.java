package com.bytescolab.featureflag.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@ToString(exclude = "configs")
public class Feature {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;

    private String description;

    private Boolean enabledByDefault;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL)
    private List<FeatureConfig> configs;

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

}
