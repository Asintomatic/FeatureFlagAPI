package com.bytescolab.featureflag.dto.auth.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType;
    private String username;
    private String role;
    private Long expiresAt;
}
