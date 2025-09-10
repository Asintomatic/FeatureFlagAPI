package com.bytescolab.featureflag.mapper;


import com.bytescolab.featureflag.dto.auth.request.RegisterRequestDTO;
import com.bytescolab.featureflag.dto.auth.request.UserRoleUpdateRequestDTO;
import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.model.enums.Role;

public class UserMapper {

    public static User toEntity(RegisterRequestDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(Role.USER)
                .build();
    }

    public static void applyRoleUpdate(User user, UserRoleUpdateRequestDTO dto) {
        user.setRole(Role.valueOf(dto.getRole()));
    }
}
