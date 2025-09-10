package com.bytescolab.featureflag.service.auth;

import com.bytescolab.featureflag.dto.auth.response.AuthResponseDTO;
import com.bytescolab.featureflag.dto.auth.request.LoginRequestDTO;
import com.bytescolab.featureflag.dto.auth.request.RegisterRequestDTO;

public interface AuthService {

    AuthResponseDTO register(RegisterRequestDTO dto);

    AuthResponseDTO login(LoginRequestDTO dto);
}
