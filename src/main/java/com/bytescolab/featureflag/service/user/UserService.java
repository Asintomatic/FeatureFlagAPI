package com.bytescolab.featureflag.service.user;

import com.bytescolab.featureflag.exception.ApiException;
import com.bytescolab.featureflag.exception.ErrorCodes;
import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.model.enums.Role;
import com.bytescolab.featureflag.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService ( UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public String updateUserRole(String name, String newRole) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND, ErrorCodes.USER_NOT_FOUND_MSG));

        Role role = Role.valueOf(newRole.toUpperCase());
        user.setRole(role);
        userRepository.save(user);

        String msg = String.format("Usuario %s actualizado correctamente con rol: %s", name, role.name());
        log.info("Usuario {} actualizado correctamente con rol: {}", name, role.name());
        return msg;
    }
}
