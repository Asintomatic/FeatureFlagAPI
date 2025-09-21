package com.bytescolab.featureflag.service;

import com.bytescolab.featureflag.config.security.auth.CustomUserDetails;
import com.bytescolab.featureflag.exception.ApiException;
import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.model.enums.Role;
import com.bytescolab.featureflag.repository.UserRepository;
import com.bytescolab.featureflag.service.user.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_ok() {
        User user = User.builder().username("pepe").password("h").role(Role.USER).build();
        when(userRepository.findByUsername("pepe")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("pepe");
        assertTrue(details instanceof CustomUserDetails);
        assertEquals("pepe", details.getUsername());
    }

    @Test
    void loadUserByUsername_notFound() {
        when(userRepository.findByUsername("nope")).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> service.loadUserByUsername("nope"));
    }
}