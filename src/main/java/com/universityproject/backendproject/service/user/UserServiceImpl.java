package com.universityproject.backendproject.service.user;

import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.user.request.UserAdvancedInfoRequest;
import com.universityproject.backendproject.model.dto.user.request.UserBasicInfoRequest;
import com.universityproject.backendproject.model.dto.user.response.*;
import com.universityproject.backendproject.model.entity.*;
import com.universityproject.backendproject.model.enums.UserRoleEnum;
import com.universityproject.backendproject.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AnimalRepository animalRepository;
    private final WalkHistoryRepository walkHistoryRepository;
    private final UserRoleRepository userRoleRepository;
    private final AdvancedUserDetailsRepository advancedUserDetailsRepository;

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
        User user = this.userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        UserBasicInfoResponse response = this.modelMapper.map(user, UserBasicInfoResponse.class);

        if (user.getRole() != null && user.getRole().getRole() != null) {
            response.setRole(user.getRole().getRole().name());
        }

        return response;
    }

    @Override
    public UserAdvancedInfoResponse getAdvancedUserInfoById(Long userId) {
        AdvancedUserDetails advancedUserDetails = this.advancedUserDetailsRepository.findByUserId(userId).orElse(null);
        return (advancedUserDetails != null) ? this.modelMapper.map(advancedUserDetails, UserAdvancedInfoResponse.class) : null;
    }

    @Override
    public byte[] getUserProfileImageById(Long id) {
        User user = this.userRepository.findById(id).orElse(null);
        return (user != null) ? user.getImg() : null;
    }


    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserIdResponse findById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        return modelMapper.map(user, UserIdResponse.class);
    }

    @Override
    public List<UserBasicInfoResponse> getRandomUsers() {
        List<User> users = userRepository.findAll();
        Random random = new Random();

        Collections.shuffle(users, random);

        return users.stream()
                .limit(3)
                .map(user -> modelMapper.map(user, UserBasicInfoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAvailableResponse> findAllAvailable() {
        UserRole userRole = userRoleRepository.findByRole(UserRoleEnum.USER);

        List<User> users = userRepository.findAllByAnimalIsNullAndRole(userRole);

        return users.stream()
                .map(user -> modelMapper.map(user, UserAvailableResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAvailableResponse> findAllUsers() {
        UserRole userRole = userRoleRepository.findByRole(UserRoleEnum.USER);

        return userRepository.findAllByRole(userRole)
                .stream()
                .map(user -> modelMapper.map(user, UserAvailableResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void takeOnWalk(Long userId, Long animalId) throws Exception {
        if (userRepository.findById(userId).isPresent() && animalRepository.findById(animalId).isPresent()) {
            User user = userRepository.findById(userId).get();
            Animal animal = animalRepository.findById(animalId).get();

            if (user.getAnimal() != null || !animal.isAvailability()) {
                throw new Exception("Someone is occupied");
            }

            animal.setAvailability(!animal.isAvailability());
            animal.setUser(user);
            user.setAnimal(animal);

            animalRepository.save(animal);
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found.");
        }

    }

    @Override
    public void returnFromWalk(Long userId, Long animalId) {
        if (userRepository.findById(userId).isPresent() && animalRepository.findById(animalId).isPresent()) {
            User user = userRepository.findById(userId).get();
            Animal animal = animalRepository.findById(animalId).get();

            WalkHistory walkHistory = new WalkHistory();

            walkHistory.setUser(user);
            walkHistory.setAnimalName(animal.getName());
            walkHistory.setAnimalType(animal.getType());
            walkHistory.setLocalDate(LocalDate.now());

            animal.setAvailability(!animal.isAvailability());
            animal.setUser(null);
            user.setAnimal(null);

            animalRepository.save(animal);
            userRepository.save(user);
            walkHistoryRepository.save(walkHistory);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found.");
        }

    }

    @Override
    public void updateBasicUserInfo(UserBasicInfoRequest request) {
        Optional<User> userOptional = this.userRepository.findById(request.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (request.getUsername() != null) {
                user.setUsername(request.getUsername());
            }
            if (request.getFirstName() != null) {
                user.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                user.setLastName(request.getLastName());
            }
            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(request.getPassword());
                user.setPassword(hashedPassword);
            }

            this.userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public void updateAdvancedUserInfo(UserAdvancedInfoRequest request) {
        Optional<User> userOptional = this.userRepository.findById(request.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            AdvancedUserDetails advancedUserDetails = user.getAdvancedUserDetails();
            if (advancedUserDetails == null) {
                advancedUserDetails = new AdvancedUserDetails();
                advancedUserDetails.setUser(user);
            }

            this.modelMapper.map(request, advancedUserDetails);

            this.advancedUserDetailsRepository.save(advancedUserDetails);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public void uploadUserProfileImage(Long userId, MultipartFile image) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            try {
                user.setImg(image.getBytes());

                this.userRepository.save(user);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }


    @Override
    public UserFirstAndLastNameResponse getFirstAndLastName(Long id) {
        User user = this.userRepository.findById(id).orElse(null);
        return (user != null) ?  this.modelMapper.map(user, UserFirstAndLastNameResponse.class): null;
    }
}
