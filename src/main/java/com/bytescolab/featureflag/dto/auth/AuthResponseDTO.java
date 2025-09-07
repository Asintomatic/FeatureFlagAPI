package com.bytescolab.featureflag.dto.auth;

import lombok.*;

import java.util.UUID;

// Respuesta "segura": no devolvemos password
@Getter @Setter @AllArgsConstructor @Builder
public class AuthResponseDTO {
    private String accessToken;
    private long expiresAt;
    @Builder.Default
    private String tokenType = "Bearer";
    private UUID id;
    private String username;
    private String role;
}
