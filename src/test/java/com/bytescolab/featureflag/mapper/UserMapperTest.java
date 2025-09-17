package com.bytescolab.featureflag.mapper;

import com.bytescolab.featureflag.dto.auth.request.RegisterRequestDTO;
import com.bytescolab.featureflag.dto.auth.request.UserRoleUpdateRequestDTO;
import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.model.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toEntity_mapsFields_andSetsDefaultRoleUser() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .username("john")
                .password("secret123")
                .build();

        User user = UserMapper.toEntity(dto);

        assertEquals("john", user.getUsername());
        assertEquals("secret123", user.getPassword());
        assertEquals(Role.USER, user.getRole()); // por defecto
    }

    @Test
    void applyRoleUpdate_updatesRole() {
        User user = User.builder()
                .username("alice")
                .password("pwd")
                .role(Role.USER)
                .build();

        UserRoleUpdateRequestDTO dto = new UserRoleUpdateRequestDTO();
        dto.setRole("ADMIN");

        UserMapper.applyRoleUpdate(user, dto);

        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void applyRoleUpdate_invalidRole_throwsException() {
        User user = User.builder().username("bob").password("pwd").role(Role.USER).build();
        UserRoleUpdateRequestDTO dto = new UserRoleUpdateRequestDTO();
        dto.setRole("INVALID_ROLE");

        assertThrows(IllegalArgumentException.class, () -> UserMapper.applyRoleUpdate(user, dto));
    }
}
