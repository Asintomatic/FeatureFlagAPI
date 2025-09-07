package com.bytescolab.featureflag.model.entity;

import com.bytescolab.featureflag.model.enums.Role;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity                     // ← esta clase es una tabla
@Table(name = "users")      // ← nombre explícito
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

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
