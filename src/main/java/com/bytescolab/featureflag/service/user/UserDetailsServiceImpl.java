package com.bytescolab.featureflag.service.user;

import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.repository.UserRepository;
import com.bytescolab.featureflag.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService; // <-- este import

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Carga un usuario desde la base de datos por su username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new CustomUserDetails(user);
    }
}
