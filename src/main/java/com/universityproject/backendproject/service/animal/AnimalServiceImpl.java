package com.universityproject.backendproject.service.animal;

import com.universityproject.backendproject.exception.AnimalNotFoundException;
import com.universityproject.backendproject.exception.UserNotFoundException;
import com.universityproject.backendproject.model.dto.animal.request.AnimalRequest;
import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalWalkResponse;
import com.universityproject.backendproject.model.dto.comment.response.CommentResponse;
import com.universityproject.backendproject.model.entity.Animal;
import com.universityproject.backendproject.model.entity.Comment;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.repository.AnimalRepository;
import com.universityproject.backendproject.repository.CommentReactionRepository;
import com.universityproject.backendproject.repository.CommentRepository;
import com.universityproject.backendproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private static final Logger log = LoggerFactory.getLogger(AnimalServiceImpl.class);

    private final AnimalRepository animalRepository;
    private final CommentReactionRepository commentReactionRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public List<AnimalResponse> findAll() {
        log.info("Fetching all animals");
        return this.animalRepository.findAll().stream()
                .map(animal -> this.modelMapper.map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<AnimalResponse> findAllPaginated(int page, int size) {
        log.info("Fetching paginated animals - Page: {}, Size: {}", page, size);
        return this.animalRepository.findAll(PageRequest.of(page, size))
                .map(animal -> this.modelMapper.map(animal, AnimalResponse.class));
    }

    @Override
    public List<AnimalResponse> findRandomAvailableAnimals() {
        log.info("Fetching random available animals");
        List<Animal> availableAnimals = this.animalRepository.findByAvailabilityTrue();
        Collections.shuffle(availableAnimals, new Random());
        return availableAnimals.stream()
                .limit(3)
                .map(animal -> this.modelMapper.map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public AnimalResponse findAnimalById(Long id, Long userId) {
        log.info("Fetching animal by ID: {} for user ID: {}", id, userId);
        Animal animal = this.animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException("Animal not found with ID: " + id));

        AnimalResponse animalResponse = this.modelMapper.map(animal, AnimalResponse.class);
        List<CommentResponse> commentResponses = animal.getComments().stream()
                .map(comment -> {
                    CommentResponse commentResponse = this.modelMapper.map(comment, CommentResponse.class);
                    User commentUser = this.userRepository.findById(comment.getUser().getId())
                            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + comment.getUser().getId()));
                    commentResponse.setUsername(commentUser.getUsername());
                    this.commentReactionRepository.findByUserIdAndCommentId(userId, comment.getId())
                            .ifPresent(reaction -> commentResponse.setCurrentUserReactionType(reaction.getReactionType()));
                    return commentResponse;
                }).collect(Collectors.toList());

        animalResponse.setComments(commentResponses);
        return animalResponse;
    }

    @Override
    public Page<AnimalWalkResponse> findAllAvailableAnimals(int page, int size) {
        log.info("Fetching all available animals - Page: {}, Size: {}", page, size);
        return this.animalRepository.findByAvailabilityTrue(PageRequest.of(page, size))
                .map(animal -> {
                    AnimalWalkResponse response = this.modelMapper.map(animal, AnimalWalkResponse.class);
                    if (animal.getUser() != null) {
                        User user = this.userRepository.findById(animal.getUser().getId()).orElse(null);
                        if (user != null) {
                            response.setUsername(user.getUsername());
                        }
                    }
                    if (animal.getImg() != null) {
                        response.setImg(Base64.getEncoder().encodeToString(animal.getImg()));
                    }
                    return response;
                });
    }

    @Override
    @Transactional
    public void updateAnimalRating(Long animalId) {
        log.info("Updating animal rating for ID: {}", animalId);
        Animal animal = this.animalRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException("Animal not found with ID: " + animalId));

        double averageRating = this.commentRepository.findAllByAnimalId(animalId).stream()
                .mapToInt(Comment::getRating)
                .average()
                .orElse(0.0);

        animal.setRating(averageRating);
        this.animalRepository.save(animal);
    }

    @Override
    @Transactional
    public void updateAnimal(Long id, AnimalRequest animalRequest, MultipartFile img) {
        log.info("Updating animal with ID: {}", id);
        Animal existingAnimal = this.animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException("Animal not found with ID: " + id));

        this.modelMapper.map(animalRequest, existingAnimal);
        if (img != null && !img.isEmpty()) {
            try {
                existingAnimal.setImg(img.getBytes());
            } catch (IOException ignored){
                throw new AnimalNotFoundException("Image not available");
            }
        }
        this.animalRepository.save(existingAnimal);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting animal with ID: {}", id);
        if (!this.animalRepository.existsById(id)) {
            throw new AnimalNotFoundException("Animal not found with ID: " + id);
        }
        this.animalRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void createAnimal(AnimalRequest request, MultipartFile img) {
        log.info("Creating a new animal");
        Animal animal = this.modelMapper.map(request, Animal.class);
        animal.setAvailability(true);
        if (img != null && !img.isEmpty()) {
            try {
                animal.setImg(img.getBytes());
            } catch (IOException ignored){
                throw new AnimalNotFoundException("Image not available");
            }
        }
        this.animalRepository.save(animal);
    }
}
