package com.bytescolab.featureflag.service.auth;

import com.bytescolab.featureflag.repository.dto.auth.response.AuthRegisterResponseDTO;
import com.bytescolab.featureflag.repository.dto.auth.response.AuthResponseDTO;
import com.bytescolab.featureflag.repository.dto.auth.request.LoginRequestDTO;
import com.bytescolab.featureflag.repository.dto.auth.request.RegisterRequestDTO;

public interface AuthService {

    AuthRegisterResponseDTO register(RegisterRequestDTO dto);

    AuthResponseDTO login(LoginRequestDTO dto);
}
