package com.universityproject.backendproject.service.authentication;

import com.universityproject.backendproject.exception.*;
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
import com.universityproject.backendproject.service.jwt.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        log.info("Attempting to reset password for user with token: {}", request.getToken());
        try {
            User user = this.jwtService.validateTokenAndGetUser(request.getToken());
            if (user == null) {
                log.error("Invalid token provided: {}", request.getToken());
                throw new InvalidTokenException("Invalid token");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            this.userRepository.save(user);
            log.info("Password reset successfully for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to reset password: {}", e.getMessage());
            throw new JwtTokenException(e.getMessage());
        }
    }

    @Override
    public void sendPasswordResetLink(String email) {
        log.info("Sending password reset link to email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        try {
            String token = jwtService.generateToken(user);
            String resetLink = "http://localhost:4200/authentication/reset-password?token=" + token;
            sendEmail(user, resetLink);
            log.info("Password reset link sent to user: {}", email);
        } catch (Exception e) {
            log.error("Failed to send password reset link: {}", e.getMessage());
            throw new JwtTokenException(e.getMessage());
        }
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Registering new user with username: {}", request.getUsername());

        if (this.userRepository.existsByUsername(request.getUsername())) {
            log.error("Username '{}' is already taken", request.getUsername());
            throw new DuplicateUserException("Username '" + request.getUsername() + "' is already taken");
        }
        if (this.userRepository.existsByEmail(request.getEmail())) {
            log.error("Email '{}' is already registered", request.getEmail());
            throw new DuplicateUserException("Email '" + request.getEmail() + "' is already registered");
        }

        UserRoleEnum userRoleEnum = Arrays.stream(UserRoleEnum.values())
                .filter(role -> role.name().equalsIgnoreCase(request.getRole()))
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException("Role '" + request.getRole() + "' not found"));

        UserRole role = Optional.ofNullable(userRoleRepository.findByRole(userRoleEnum))
                .orElseThrow(() -> new RoleNotFoundException("Role '" + userRoleEnum + "' not found in the database"));


        User user = User.builder()
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

        log.info("User registered successfully: {}", user.getEmail());

        try {
            var jwtToken = this.jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            log.error("Failed to generate JWT token: {}", e.getMessage());
            throw new JwtTokenException(e.getMessage());
        }
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticating user with email: {}", request.getEmail());
        var user = this.userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", request.getEmail());
                    return new UserNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Invalid password for user: {}", request.getEmail());
            throw new InvalidPasswordException("Invalid password");
        }

        try {
            var jwtToken = this.jwtService.generateToken(user);
            log.info("User authenticated successfully: {}", user.getEmail());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            log.error("Failed to generate JWT token: {}", e.getMessage());
            throw new JwtTokenException(e.getMessage());
        }
    }

    private void sendEmail(User user, String resetLink) {
        log.info("Sending password reset email to: {}", user.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset Request");

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Dear ").append(user.getUsername()).append(",\n\n");
        emailContent.append("We received a request to reset your password. To complete the process, please click the link below:\n\n");
        emailContent.append(resetLink).append("\n\n");
        emailContent.append("Please note that this link will expire in ").append(1).append(" day.\n");
        emailContent.append("If you did not request a password reset, please ignore this email or contact our support team.\n\n");
        emailContent.append("Best regards,\n");
        emailContent.append("Your Support Team");

        message.setText(emailContent.toString());
        this.javaMailSender.send(message);
        log.info("Password reset email sent to: {}", user.getEmail());
    }

}
