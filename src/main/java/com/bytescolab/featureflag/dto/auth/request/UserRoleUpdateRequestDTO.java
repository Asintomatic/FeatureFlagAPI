package com.bytescolab.featureflag.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleUpdateRequestDTO {

    @NotBlank (message = "Role es necesario")
    @Schema(example = "ADMIN", description = "Rol que se aplica al usuario (USER, ADMIN)")
    private String role;
}
