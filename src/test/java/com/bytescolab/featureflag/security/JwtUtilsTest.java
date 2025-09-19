package com.bytescolab.featureflag.security;

import com.bytescolab.featureflag.config.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Set secret and expiration via reflection for tests
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "super-secret-key-for-tests-12345678901234567890");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000L); // 1h
    }

    @Test
    void generateAndValidateToken_ok() {
        UserDetails user = User.withUsername("pepe").password("x").authorities("ROLE_USER").build();
        String token = jwtUtils.generateToken(user);
        assertNotNull(token);
        assertEquals("pepe", jwtUtils.extractUsername(token));
        assertTrue(jwtUtils.extractExpirationMillis(token) > System.currentTimeMillis());
    }
}