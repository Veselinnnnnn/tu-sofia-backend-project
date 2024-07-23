package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.animal.request.AnimalRequest;
import com.universityproject.backendproject.model.dto.animal.response.AnimalAvailableResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalWalkResponse;
import com.universityproject.backendproject.model.dto.user.response.UserIdResponse;
import com.universityproject.backendproject.service.animal.AnimalService;
import com.universityproject.backendproject.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/animal")
@AllArgsConstructor
public class AnimalController {

    private final AnimalService animalService;
    private final UserService userService;

    @GetMapping("page/{page}")
    public Page<AnimalResponse> getAllAnimals(@PathVariable int page) {
        return animalService.findAll(page);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/volunteer")
    public UserIdResponse getVolunteerFor(@PathVariable Long id) {

        Long userId = animalService.findAnimalById(id).getUserId();
        return userService.findById(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public void createAnimal(@RequestBody AnimalRequest animalRequest) {
        try {
            animalService.createAnimal(animalRequest);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Write it right !!!", e);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "All ready exist !!!", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/isAvailable/{id}")
    public AnimalAvailableResponse isAvailable(@PathVariable Long id) {
        try {
            return animalService.findAnimalById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found !!!", e);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/onWalk/page/{page}")
    public Page<AnimalWalkResponse> onWalk(@PathVariable int page) {
        try {
            return animalService.findAllAvailable(page);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // TODO ENVERS HIBERNATE DEPENDENCY AND USE @SLf4J LOGGER
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/adopt/{id}")
    public void adoptAnimal(@PathVariable Long id) {
        try {
            animalService.adoptAnimal(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal not found");
        }
    }
}
