package com.bytescolab.featureflag.model.entity;

import com.bytescolab.featureflag.model.enums.Role;

import java.util.UUID;

public class User {

    private UUID id;
    private String username;
    private String password;
    private Role rol;

}
