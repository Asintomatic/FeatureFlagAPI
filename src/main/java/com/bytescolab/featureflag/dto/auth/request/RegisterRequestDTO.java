package com.bytescolab.featureflag.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 16, message = "La contraseña debe tener entre 6 y 16 caracteres")
    @Pattern(regexp = ".*\\d.*", message = "La contraseña debe contener al menos un número")
    private String password;
}
