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
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterDTO dto) {

        if (users.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        String hash = encoder.encode(dto.getPassword());

        User u = User.builder()
                .username(dto.getUsername())
                .password(hash)
                .role(Role.USER)
                .build();

        User saved = users.save(u);

        UserDetails details = new CustomUserDetails(saved);

        String token = jwtUtils.generateToken(details);
        long expMillis = jwtUtils.extractExpirationMillis(token);
        return AuthResponseDTO.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .role(saved.getRole().name())
                .accessToken(token)
                .expiresAt(expMillis)
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
