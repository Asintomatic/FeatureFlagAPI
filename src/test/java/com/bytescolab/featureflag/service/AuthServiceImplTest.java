package com.bytescolab.featureflag.service;

import com.bytescolab.featureflag.dto.auth.request.LoginRequestDTO;
import com.bytescolab.featureflag.dto.auth.request.RegisterRequestDTO;
import com.bytescolab.featureflag.dto.auth.response.AuthRegisterResponseDTO;
import com.bytescolab.featureflag.dto.auth.response.AuthResponseDTO;
import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.model.enums.Role;
import com.bytescolab.featureflag.repository.UserRepository;
import com.bytescolab.featureflag.security.jwt.JwtUtils;
import com.bytescolab.featureflag.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ok_whenNewUser() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .username("pepe")
                .password("password")
                .build();

        when(userRepository.existsByUsername("pepe")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashed");
        User saved = User.builder().username("pepe").role(Role.USER).password("hashed").build();
        when(userRepository.save(any(User.class))).thenReturn(saved);

        AuthRegisterResponseDTO res = authService.register(dto);
        assertEquals("pepe", res.getUsername());
        assertEquals("USER", res.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_adminRoleIfUsernameAdmin() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .username("admin")
                .password("x")
                .build();

        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(passwordEncoder.encode("x")).thenReturn("h");
        User saved = User.builder().username("admin").role(Role.ADMIN).password("h").build();
        when(userRepository.save(any(User.class))).thenReturn(saved);

        AuthRegisterResponseDTO res = authService.register(dto);
        assertEquals("ADMIN", res.getRole());
    }

    @Test
    void login_ok_returnsTokenAndClaims() {
        LoginRequestDTO dto = LoginRequestDTO.builder()
                .username("pepe")
                .password("secret")
                .build();

        UserDetails principal = new org.springframework.security.core.userdetails.User(
                "pepe", "hashed", List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(jwtUtils.generateToken(principal)).thenReturn("token-123");
        when(jwtUtils.extractExpirationMillis("token-123")).thenReturn(123456789L);

        AuthResponseDTO res = authService.login(dto);
        assertEquals("token-123", res.getAccessToken());
        assertEquals(123456789L, res.getExpiresAt());
        assertEquals("Bearer", res.getTokenType());
        assertEquals("pepe", res.getUsername());
        assertEquals("USER", res.getRole());
    }
}