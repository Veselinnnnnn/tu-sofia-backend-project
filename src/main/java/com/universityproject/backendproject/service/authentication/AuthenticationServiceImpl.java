package com.universityproject.backendproject.service.authentication;


import com.universityproject.backendproject.model.dto.authentication.request.AuthenticationRequest;
import com.universityproject.backendproject.model.dto.authentication.request.RegisterRequest;
import com.universityproject.backendproject.model.dto.authentication.response.AuthenticationResponse;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.model.entity.UserRole;
import com.universityproject.backendproject.model.enums.UserRoleEnum;
import com.universityproject.backendproject.repository.UserRepository;
import com.universityproject.backendproject.repository.UserRoleRepository;
import com.universityproject.backendproject.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UserRoleRepository userRoleRepository;

    @Override
    public AuthenticationResponse register(RegisterRequest request) throws Exception {
        UserRole role = userRoleRepository.findByRole(UserRoleEnum.USER);

        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
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

        var jwtToken = this.jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
