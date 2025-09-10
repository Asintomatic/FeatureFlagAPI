package com.bytescolab.featureflag.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
