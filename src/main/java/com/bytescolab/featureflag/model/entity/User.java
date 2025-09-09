package com.bytescolab.featureflag.model.entity;

import com.bytescolab.featureflag.model.enums.Role;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405172041950251807L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
