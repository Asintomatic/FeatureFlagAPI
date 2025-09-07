package com.bytescolab.featureflag.service.auth;

import com.bytescolab.featureflag.dto.auth.LoginDTO;
import com.bytescolab.featureflag.dto.auth.RegisterDTO;
import com.bytescolab.featureflag.dto.auth.AuthResponseDTO;
import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.model.enums.Role;
import com.bytescolab.featureflag.repository.UserRepository;
import com.bytescolab.featureflag.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import com.bytescolab.featureflag.security.jwt.JwtUtils;

@Service
@RequiredArgsConstructor // inyecta repositorio y encoder por constructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder; // BCrypt (lo definimos en SecurityConfig)
    // 👇 NUEVO (lo usa login() para validar credenciales con Spring Security)
    private final AuthenticationManager authenticationManager;

    // 👇 NUEVO (lo usa login() para emitir el JWT)
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterDTO dto) {
        // 1) Regla: username único
        if (users.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        // 2) Nunca se debe guardar password en claro → usa el hash
        String hash = encoder.encode(dto.getPassword());

        // 3) Construimos la entidad con rol por defecto USER (evita escaladas)
        User u = User.builder()
                .username(dto.getUsername())
                .password(hash)
                .role(Role.USER)
                .build();

        // 4) Guardamos y devolvemos DTO de salida sin campos sensibles
        User saved = users.save(u);
        // Construye un UserDetails para emitir el token; puedes reusar tu CustomUserDetails
        UserDetails details = new CustomUserDetails(saved);

        String token = jwtUtils.generateToken(details);
        long expMillis = jwtUtils.extractExpirationMillis(token);
        return AuthResponseDTO.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .role(saved.getRole().name())
                .accessToken(token)        // <- ya no es null
                .expiresAt(expMillis)      // <- ya no es 0
                .tokenType("Bearer")
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        UserDetails principal = (UserDetails) auth.getPrincipal();
        String role = principal.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        String token = jwtUtils.generateToken(principal);
        long expMillis = jwtUtils.extractExpirationMillis(token);

        return AuthResponseDTO.builder()
                .accessToken(token)
                .expiresAt(expMillis)
                .tokenType("Bearer")
                .username(principal.getUsername())
                .role(role)
                .build();
    }
}
