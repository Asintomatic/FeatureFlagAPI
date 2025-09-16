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


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication endpoints")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario", description = "Registro de un usuario")
    public ResponseEntity<AuthRegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auth.register(dto));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Valida credenciales y devuelve un JWT (Bearer) con la expiración y el rol")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(auth.login(dto));
    }
}
