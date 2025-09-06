package com.bytescolab.featureflag.service.auth;

import com.bytescolab.featureflag.dto.auth.AuthResponseDTO;
import com.bytescolab.featureflag.dto.auth.RegisterDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterDTO dto);
}
