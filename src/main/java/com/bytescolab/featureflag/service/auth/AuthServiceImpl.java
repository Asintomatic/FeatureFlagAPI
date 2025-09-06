package com.bytescolab.featureflag.service.auth;

import com.bytescolab.featureflag.dto.auth.RegisterDTO;
import com.bytescolab.featureflag.dto.auth.AuthResponseDTO;
import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.model.enums.Role;
import com.bytescolab.featureflag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // inyecta repositorio y encoder por constructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder; // BCrypt (lo definimos en SecurityConfig)

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
        return AuthResponseDTO.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .role(saved.getRole().name())
                .build();
    }
}
