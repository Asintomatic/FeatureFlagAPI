package com.bytescolab.featureflag.model.entity;


import com.bytescolab.featureflag.model.enums.Environment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


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
    @Enumerated(EnumType.STRING)
    private Environment environment;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(nullable = false)
    private Boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

}
