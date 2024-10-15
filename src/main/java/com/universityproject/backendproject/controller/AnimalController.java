package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.animal.request.AnimalRequest;
import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalWalkResponse;
import com.universityproject.backendproject.service.animal.AnimalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/animal")
@AllArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("/all")
    public List<AnimalResponse> findAllAnimals() {
        return animalService.findAll();
    }

    @GetMapping("/all/paginated")
    public Page<AnimalResponse> findAllAnimalsPaginated(@RequestParam int page, @RequestParam int size) {
        return animalService.findAllPaginated(page,size);
    }

    @GetMapping("/random")
    public List<AnimalResponse> findRandomAvailableAnimals() {
        return animalService.findRandomAvailableAnimals();
    }

    @GetMapping("")
    public AnimalResponse findAnimalById(@RequestParam Long animalId, @RequestParam Long userId) {
        try {
            return animalService.findAnimalById(animalId, userId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/available")
    public Page<AnimalWalkResponse> findAllAvailableAnimals(@RequestParam int page, @RequestParam int size) {
        try {
            return animalService.findAllAvailableAnimals(page, size);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update-rating")
    public Page<AnimalWalkResponse> updateAnimalRating(@RequestParam int page, @RequestParam int size) {
        try {
            return animalService.findAllAvailableAnimals(page, size);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(consumes = {"multipart/form-data"})
    public void updateAnimal(
            @RequestParam Long id,
            @RequestParam("img") MultipartFile img,
            @ModelAttribute AnimalRequest animal // Changed here
    ) throws IOException {
        this.animalService.updateAnimal(id, animal, img);
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("/{id}/volunteer")
//    public UserIdResponse getVolunteerFor(@PathVariable Long id) {
//
//        Long userId = animalService.findAnimalById(id).getUserId();
//        return userService.findById(userId);
//    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(consumes = {"multipart/form-data"})
    public void createAnimal(
            @RequestPart("img") MultipartFile img,
            @RequestPart("animal") AnimalRequest animal
    ) {
        try {
            animalService.createAnimal(animal, img);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Write it right !!!", e);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "All ready exist !!!", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping()
    public void deleteAnimal(@RequestParam Long id) {
        animalService.delete(id);
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("/isAvailable/{id}")
//    public AnimalAvailableResponse isAvailable(@PathVariable Long id) {
//        try {
//            return animalService.findAnimalById(id);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found !!!", e);
//        }
//    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
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
