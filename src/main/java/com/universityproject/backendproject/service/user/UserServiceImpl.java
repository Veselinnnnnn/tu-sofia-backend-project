package com.universityproject.backendproject.service.user;

import com.universityproject.backendproject.exception.UserNotFoundException;
import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.user.request.UserAdvancedInfoRequest;
import com.universityproject.backendproject.model.dto.user.request.UserBasicInfoRequest;
import com.universityproject.backendproject.model.dto.user.response.*;
import com.universityproject.backendproject.model.entity.*;
import com.universityproject.backendproject.model.enums.UserRoleEnum;
import com.universityproject.backendproject.repository.*;
import com.universityproject.backendproject.service.jwt.JwtServiceImpl;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

    private final AdvancedUserDetailsRepository advancedUserDetailsRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserBasicInfoResponse> getAllMembers() {
        List<UserRoleEnum> roles = Arrays.asList(UserRoleEnum.TEAM, UserRoleEnum.ADMIN);
        List<User> members = userRepository.findByRole_RoleIn(roles);
        return members.stream()
                .map(member -> modelMapper.map(member, UserBasicInfoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserBasicInfoResponse getBasicUserInfoById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found with id: {}", id);
            throw new UserNotFoundException("User not found");
        });

        UserBasicInfoResponse response = modelMapper.map(user, UserBasicInfoResponse.class);
        response.setRole(Optional.ofNullable(user.getRole()).map(role -> role.getRole().name()).orElse(null));
        return response;
    }

    @Override
    public UserAdvancedInfoResponse getAdvancedUserInfoById(Long userId) {
        AdvancedUserDetails advancedUserDetails = advancedUserDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Advanced user details not found for userId: {}", userId);
                    return new UserNotFoundException("User not found");
                });
        return modelMapper.map(advancedUserDetails, UserAdvancedInfoResponse.class);
    }

    @Override
    public byte[] getUserProfileImageById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found with id: {}", id);
            return new UserNotFoundException("User not found");
        });
        return user.getImg();
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found with id: {}", id);
            return new UserNotFoundException("User not found");
        });
    }

    @Override
    public UserIdResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found with id: {}", id);
            return new UserNotFoundException("User not found");
        });
        return modelMapper.map(user, UserIdResponse.class);
    }

    @Override
    public List<UserBasicInfoResponse> getRandomUsers() {
        List<User> users = userRepository.findAll();
        Collections.shuffle(users);
        return users.stream()
                .limit(3)
                .map(user -> modelMapper.map(user, UserBasicInfoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateBasicUserInfo(UserBasicInfoRequest request) {
        User user = userRepository.findById(request.getId()).orElseThrow(() -> {
            log.warn("User not found with id: {}", request.getId());
            return new UserNotFoundException("User not found");
        });

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(hashedPassword);
        }

        userRepository.save(user);
        log.info("Updated basic user info for userId: {}", request.getId());
    }

    @Override
    public void updateAdvancedUserInfo(UserAdvancedInfoRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            log.warn("User not found with id: {}", request.getUserId());
            return new UserNotFoundException("User not found");
        });

        AdvancedUserDetails advancedUserDetails = Optional.ofNullable(user.getAdvancedUserDetails())
                .orElse(new AdvancedUserDetails());

        modelMapper.map(request, advancedUserDetails);
        advancedUserDetails.setUser(user); // Ensure the user reference is set

        advancedUserDetailsRepository.save(advancedUserDetails);
        log.info("Updated advanced user info for userId: {}", request.getUserId());
    }

    @Override
    public void uploadUserProfileImage(Long userId, MultipartFile image) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User not found with id: {}", userId);
            return new UserNotFoundException("User not found");
        });

        try {
            user.setImg(image.getBytes());
            userRepository.save(user);
            log.info("Uploaded profile image for userId: {}", userId);
        } catch (IOException e) {
            log.error("Failed to upload image for userId: {}", userId, e);
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public UserFirstAndLastNameResponse getFirstAndLastName(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found with id: {}", id);
            return new UserNotFoundException("User not found");
        });
        return modelMapper.map(user, UserFirstAndLastNameResponse.class);
    }
}
