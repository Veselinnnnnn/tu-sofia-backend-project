package com.universityproject.backendproject.service.authentication;

import com.universityproject.backendproject.model.dto.authentication.request.AuthenticationRequest;
import com.universityproject.backendproject.model.dto.authentication.request.RegisterRequest;
import com.universityproject.backendproject.model.dto.authentication.request.ResetPasswordRequest;
import com.universityproject.backendproject.model.dto.authentication.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void sendPasswordResetLink(String email);

    void resetPassword(ResetPasswordRequest request);
}
