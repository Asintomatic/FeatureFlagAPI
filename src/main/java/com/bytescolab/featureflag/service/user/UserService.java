package com.bytescolab.featureflag.service.user;

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
    public String updateUserRole(UUID userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        user.setRole(newRole);
        userRepository.save(user);

        String msg = String.format("Usuario %s actualizado correctamente con rol: %s", user.getUsername(), newRole.name());
        log.info("Usuario {} actualizado correctamente con rol: {}", user.getUsername(), newRole.name());
        return msg;
    }
}
