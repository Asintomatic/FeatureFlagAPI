package com.bytescolab.featureflag.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
