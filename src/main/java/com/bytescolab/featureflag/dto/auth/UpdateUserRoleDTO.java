package com.bytescolab.featureflag.dto.auth;

import com.bytescolab.featureflag.model.enums.Role;
import jakarta.validation.constraints.NotNull;

public class UpdateUserRoleDTO {

    @NotNull
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}