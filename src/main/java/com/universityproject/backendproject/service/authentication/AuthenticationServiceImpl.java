package com.universityproject.backendproject.service.authentication;


import com.universityproject.backendproject.model.dto.application.response.ApplicationResponse;
import com.universityproject.backendproject.model.dto.authentication.request.AuthenticationRequest;
import com.universityproject.backendproject.model.dto.authentication.request.RegisterRequest;
import com.universityproject.backendproject.model.dto.authentication.request.ResetPasswordRequest;
import com.universityproject.backendproject.model.dto.authentication.response.AuthenticationResponse;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.model.entity.UserRole;
import com.universityproject.backendproject.model.enums.UserRoleEnum;
import com.universityproject.backendproject.repository.UserRepository;
import com.universityproject.backendproject.repository.UserRoleRepository;
import com.universityproject.backendproject.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UserRoleRepository userRoleRepository;

    private final JavaMailSender javaMailSender;

    @Override
    public void resetPassword(ResetPasswordRequest request) throws Exception {
        User user = this.jwtService.validateTokenAndGetUser(request.getToken());
        if (user == null) {
            throw new Exception("Invalid token");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        this.userRepository.save(user);
    }

    @Override
    public void sendPasswordResetLink(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));

        String token = jwtService.generateToken(user);

        String resetLink = "http://localhost:4200/authentication/reset-password?token=" + token;

        sendEmail(user.getEmail(), resetLink);
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) throws Exception {
        UserRole role = userRoleRepository.findByRole(request.getRole());

        if (role == null) {
            role = userRoleRepository.findByRole(UserRoleEnum.ADMIN);
        }

        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .title(request.getTitle())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        User userResponse = this.userRepository.save(user);

        user.setId(userResponse.getId());

        var jwtToken = this.jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        var user = this.userRepository
                .findByEmail(request.getEmail())
                .orElseThrow();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Invalid password");
        }

        var jwtToken = this.jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void sendEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Password");
        message.setText("Click the link to reset your password: " + resetLink);
        this.javaMailSender.send(message);
    }
}
