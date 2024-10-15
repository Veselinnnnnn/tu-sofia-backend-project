package com.universityproject.backendproject.service.animal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universityproject.backendproject.model.dto.animal.request.AnimalRequest;
import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalWalkResponse;
import com.universityproject.backendproject.model.dto.comment.response.CommentResponse;
import com.universityproject.backendproject.model.entity.Animal;
import com.universityproject.backendproject.model.entity.Comment;
import com.universityproject.backendproject.model.entity.CommentReaction;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.repository.AnimalRepository;
import com.universityproject.backendproject.repository.CommentReactionRepository;
import com.universityproject.backendproject.repository.CommentRepository;
import com.universityproject.backendproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private final AnimalRepository animalRepository;
    private final CommentReactionRepository commentReactionRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<AnimalResponse> findAll() {
        List<Animal> animals = animalRepository.findAll();

        return animals.stream()
                .map(animal -> modelMapper.map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<AnimalResponse> findAllPaginated(int page, int size) {
        PageRequest request = PageRequest.of(page, size);

        return animalRepository
                .findAll(request)
                .map(a -> modelMapper.map(a, AnimalResponse.class));
    }

    @Override
    public List<AnimalResponse> findRandomAvailableAnimals() {
        List<Animal> availableAnimals = animalRepository.findByAvailabilityTrue();
        Random random = new Random();

        Collections.shuffle(availableAnimals, random);

        return availableAnimals.stream()
                .limit(3)
                .map(animal -> modelMapper.map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public AnimalResponse findAnimalById(Long id, Long userId) {
        Animal animal = animalRepository.findAnimalById(id);
        AnimalResponse animalResponse = modelMapper.map(animal, AnimalResponse.class);

        List<CommentResponse> commentResponses = animal.getComments().stream()
                .map(comment -> {
                    CommentResponse commentResponse = modelMapper.map(comment, CommentResponse.class);

                    // Fetch the user who made the comment
                    User commentUser = userRepository.findUserById(comment.getUser().getId());
                    if (commentUser != null) {
                        commentResponse.setUsername(commentUser.getUsername());
                    }

                    // Fetch the current user's reaction to this comment, if any
                    Optional<CommentReaction> existingReaction = commentReactionRepository.findByUserIdAndCommentId(userId, comment.getId());
                    existingReaction.ifPresent(reaction -> commentResponse.setCurrentUserReactionType(reaction.getReactionType()));

                    return commentResponse;
                })
                .collect(Collectors.toList());

        animalResponse.setComments(commentResponses);

        return animalResponse;
    }

    @Override
    public Page<AnimalWalkResponse> findAllAvailableAnimals(int page, int size) {
        PageRequest request = PageRequest.of(page, size);

        Page<Animal> animals = animalRepository.findByAvailabilityTrue(request);

        return animals
                .map(animal -> {
                    AnimalWalkResponse animalWalkResponse = modelMapper.map(animal, AnimalWalkResponse.class);


                    if (animal.getUser() != null) {
                        User user = userRepository.findById(animal.getUser().getId()).orElse(null);
                        if (user != null) {
                            animalWalkResponse.setUsername(user.getUsername());
                        }
                    }

                    if (animal.getImg() != null) {
                        String base64Image = Base64.getEncoder().encodeToString(animal.getImg());
                        animalWalkResponse.setImg(base64Image);
                        System.out.println("Base64 Image: " + base64Image); // Log Base64 image string
                    }


                    return animalWalkResponse;
                });
    }

    public void updateAnimalRating(Long animalId) {
        Animal animal = this.animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        List<Comment> comments = this.commentRepository.findAllByAnimalId(animalId);

        if (comments.isEmpty()) {
            animal.setRating(0);
        } else {
            double averageRating = comments.stream()
                    .mapToInt(Comment::getRating)
                    .average()
                    .orElse(0.0);

            animal.setRating(averageRating);
        }

        this.animalRepository.save(animal);
    }

    @Override
    public void updateAnimal(Long id, AnimalRequest animalRequest, MultipartFile img) throws IOException {
        Animal existingAnimal = animalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Animal not found with ID: " + id));

        modelMapper.map(animalRequest, existingAnimal);

        if (img != null && !img.isEmpty()) {
            existingAnimal.setImg(img.getBytes());
        }

        animalRepository.save(existingAnimal);
    }

    @Override
    public void delete(Long id) {
        if (animalRepository.existsById(id)) {
            animalRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Animal not found");
        }
    }

    @Override
    public void createAnimal(AnimalRequest request, MultipartFile img) throws IOException {
        Animal animal = modelMapper.map(request, Animal.class);
        animal.setAvailability(true);

        if (img != null && !img.isEmpty()) {
            animal.setImg(img.getBytes());
        }

        animalRepository.save(animal);
    }

    @Override
    public void adoptAnimal(Long id) {
        animalRepository.deleteById(id);
    }

//    @Override
//    public AnimalAvailableResponse findAnimalById(Long id) {
//        try {
//            Animal animal = animalRepository.findById(id).orElseThrow();
//            return modelMapper.map(animal, AnimalAvailableResponse.class);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found", e);
//        } catch (HttpClientErrorException.Unauthorized e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No accreditation !!!", e);
//        }
//
//    }

    @Override
    public Page<AnimalWalkResponse> findAllAvailable(int page) {

        PageRequest requestPage = PageRequest.of(page, 6);

        Page<Animal> animals = animalRepository.findAllAvailable(requestPage);

        return animals
                .map(animal -> {
                    System.out.println(animal);
                    AnimalWalkResponse animalWalkResponse = modelMapper
                            .map(animal, AnimalWalkResponse.class);

                    if (animal.getUser() == null) {
                        return animalWalkResponse;
                    }

                    User user = userRepository.findById(animal.getUser().getId()).orElse(null);

                    if (user == null) {
                        return animalWalkResponse;
                    }

                    animalWalkResponse.setUsername(user.getUsername());

                    return animalWalkResponse;
                });
    }
}
