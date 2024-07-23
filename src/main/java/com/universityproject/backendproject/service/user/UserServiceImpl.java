package com.universityproject.backendproject.service.user;

import com.universityproject.backendproject.model.dto.user.response.UserAvailableResponse;
import com.universityproject.backendproject.model.dto.user.response.UserIdResponse;
import com.universityproject.backendproject.model.entity.Animal;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.model.entity.UserRole;
import com.universityproject.backendproject.model.entity.WalkHistory;
import com.universityproject.backendproject.model.enums.UserRoleEnum;
import com.universityproject.backendproject.repository.AnimalRepository;
import com.universityproject.backendproject.repository.UserRepository;
import com.universityproject.backendproject.repository.UserRoleRepository;
import com.universityproject.backendproject.repository.WalkHistoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AnimalRepository animalRepository;
    private final WalkHistoryRepository walkHistoryRepository;
    private final UserRoleRepository userRoleRepository;

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
}
