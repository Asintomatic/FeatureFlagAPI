package com.bytescolab.featureflag.controller;

import com.bytescolab.featureflag.dto.auth.request.LoginRequestDTO;
import com.bytescolab.featureflag.dto.auth.request.RegisterRequestDTO;
import com.bytescolab.featureflag.dto.auth.response.AuthRegisterResponseDTO;
import com.bytescolab.featureflag.dto.auth.response.AuthResponseDTO;
import com.bytescolab.featureflag.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de autenticación en la aplicación FeatureFlag API.
 * <p>
 * Este controlador expone endpoints relacionados con el registro y el inicio de sesión
 * de usuarios. Permite crear nuevos usuarios en el sistema y autenticar credenciales
 * válidas para obtener un token JWT que habilite el acceso a los endpoints protegidos.
 * </p>
 *
 * <p><b>Endpoints principales:</b></p>
 * <ul>
 *   <li><b>POST /api/auth/register</b>: Registra un nuevo usuario en el sistema.</li>
 *   <li><b>POST /api/auth/login</b>: Autentica credenciales y devuelve un token JWT válido.</li>
 * </ul>
 *
 * <p><b>Seguridad:</b></p>
 * <p>
 * Los tokens generados en el login se deben incluir en la cabecera <i>Authorization</i> con el formato:
 * </p>
 * <pre>
 * Authorization: Bearer &lt;token-jwt&gt;
 * </pre>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.service.auth.AuthService
 * @see com.bytescolab.featureflag.dto.auth.request.RegisterRequestDTO
 * @see com.bytescolab.featureflag.dto.auth.request.LoginRequestDTO
 * @see com.bytescolab.featureflag.dto.auth.response.AuthResponseDTO
 * @see com.bytescolab.featureflag.dto.auth.response.AuthRegisterResponseDTO
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication endpoints")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param dto datos del usuario a registrar (username y password).
     * @return {@link ResponseEntity} con el objeto {@link AuthRegisterResponseDTO}
     *         y el estado HTTP {@code 201 Created} si el registro fue exitoso.
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario", description = "Registro de un usuario")
    public ResponseEntity<AuthRegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auth.register(dto));
    }

    /**
     * Autentica un usuario y genera un token JWT.
     *
     * @param dto credenciales del usuario (username y password).
     * @return {@link ResponseEntity} con un objeto {@link AuthResponseDTO}
     *         que contiene el token JWT, el rol del usuario y la fecha de expiración.
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Valida credenciales y devuelve un JWT (Bearer) con la expiración y el rol")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(auth.login(dto));
    }
}
