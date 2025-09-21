package com.bytescolab.featureflag.service.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface UserService {


    @Transactional
    public String updateUserRole(String name, String newRole);
}
