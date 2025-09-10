package com.bytescolab.featureflag.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleUpdateRequestDTO {

    @NotBlank (message = "Username es necesario")
    private String username;

    @NotBlank (message = "Role es necesario")
    private String role;
}
