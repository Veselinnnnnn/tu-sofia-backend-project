package com.universityproject.backendproject.service.authentication;

import com.universityproject.backendproject.model.dto.authentication.request.AuthenticationRequest;
import com.universityproject.backendproject.model.dto.authentication.request.RegisterRequest;
import com.universityproject.backendproject.model.dto.authentication.request.ResetPasswordRequest;
import com.universityproject.backendproject.model.dto.authentication.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request) throws Exception;

    AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception;

    void sendPasswordResetLink(String email) throws Exception;

    void resetPassword(ResetPasswordRequest request) throws Exception;
}
