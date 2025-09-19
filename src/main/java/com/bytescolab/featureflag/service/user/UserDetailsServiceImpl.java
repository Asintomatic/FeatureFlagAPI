package com.bytescolab.featureflag.service.user;

import com.bytescolab.featureflag.exception.ApiException;
import com.bytescolab.featureflag.exception.ErrorCodes;
import com.bytescolab.featureflag.model.entity.User;
import com.bytescolab.featureflag.repository.UserRepository;
import com.bytescolab.featureflag.config.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService; 

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Carga un usuario desde la base de datos por su username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws ApiException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND, ErrorCodes.USER_NOT_FOUND_MSG));

        return new CustomUserDetails(user);
    }
}
