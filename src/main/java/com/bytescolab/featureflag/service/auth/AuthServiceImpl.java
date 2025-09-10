package com.bytescolab.featureflag.service.auth;

import com.bytescolab.featureflag.dto.auth.request.LoginRequestDTO;
import com.bytescolab.featureflag.dto.auth.request.RegisterRequestDTO;
import com.bytescolab.featureflag.dto.auth.response.AuthRegisterResponseDTO;
import com.bytescolab.featureflag.dto.auth.response.AuthResponseDTO;
import com.bytescolab.featureflag.mapper.UserMapper;
import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.model.enums.Role;
import com.bytescolab.featureflag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytescolab.featureflag.security.jwt.JwtUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthRegisterResponseDTO register(RegisterRequestDTO dto) {
        if (users.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        User user = UserMapper.toEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));

        if (user.getUsername().equalsIgnoreCase("admin")) {
            user.setRole(Role.ADMIN);
        }

        User saved = users.save(user);
        log.info("Usuario {} creado con éxito.", user.getUsername());

        return AuthRegisterResponseDTO.builder()
                .username(saved.getUsername())
                .role(saved.getRole().name())
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        UserDetails principal = (UserDetails) auth.getPrincipal();
        String role = principal.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        String token = jwtUtils.generateToken(principal);
        long expMillis = jwtUtils.extractExpirationMillis(token);

        log.info("Usuario: {} logueado con Bearer: {}", dto.getUsername(), token);

        return AuthResponseDTO.builder()
                .accessToken(token)
                .expiresAt(expMillis)
                .tokenType("Bearer")
                .username(principal.getUsername())
                .role(role)
                .build();
    }
}
