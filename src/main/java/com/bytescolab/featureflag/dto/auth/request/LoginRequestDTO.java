package com.bytescolab.featureflag.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDTO {

    @NotBlank (message = "Username es necesario")
    private String username;

    @NotBlank (message = "Password es necesario")
    private String password;
}
