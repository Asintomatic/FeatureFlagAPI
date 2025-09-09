package com.bytescolab.featureflag.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {

    @NotBlank(message = "Nombre de usuario es necesario")
    @Size(min = 3, max = 30, message = "username must be 3-50 chars")
    private String username;

    @NotBlank(message = " Contraseña es necesaria")
    @Size(min = 6, max = 16, message = "La contraseña tiene que tener entre 6 y 16 carácteres.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&\\-_.])[A-Za-z\\d@$!%*#?&\\-_.]{8,}$",
            message = "La contraseña debe contener una mayúscula, una minúscula, un numero y un carácter especial."
    )
    private String password;
}
