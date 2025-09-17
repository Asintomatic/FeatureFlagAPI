package com.bytescolab.featureflag.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserDetailsService  {

    UserDetails loadUserByUsername ( String username);
}