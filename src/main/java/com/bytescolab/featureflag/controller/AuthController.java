package com.bytescolab.featureflag.controller;

import com.bytescolab.featureflag.dto.auth.LoginDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bytescolab.featureflag.dto.auth.RegisterDTO;
import com.bytescolab.featureflag.dto.auth.AuthResponseDTO;
import com.bytescolab.featureflag.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication endpoints")
@RequiredArgsConstructor
public class AuthController {

    // ⚠️ Importante: 'final' + @RequiredArgsConstructor evita el error “Field 'auth' might not have been initialized”
    private final AuthService auth;


    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a user with default role USER")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO dto) {
        // El controller NO sabe de JPA ni seguridad: delega en el servicio (DIP)
        return ResponseEntity.ok(auth.register(dto));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Valida credenciales y devuelve un JWT (Bearer) con la expiración y el rol"
    )
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        // Delegamos en el servicio: él usa AuthenticationManager (BCrypt) y JwtUtils para emitir el token
        return ResponseEntity.ok(auth.login(dto));
    }

}
