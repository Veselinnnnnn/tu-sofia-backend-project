package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.animal.request.AnimalRequest;
import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalWalkResponse;
import com.universityproject.backendproject.service.animal.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/animal")
@AllArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("/all")
    public ResponseEntity<List<AnimalResponse>> findAllAnimals() {
        List<AnimalResponse> animals = this.animalService.findAll();
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/all/paginated")
    public ResponseEntity<Page<AnimalResponse>> findAllAnimalsPaginated(@RequestParam int page, @RequestParam int size) {
        Page<AnimalResponse> animals = this.animalService.findAllPaginated(page, size);
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/random")
    public ResponseEntity<List<AnimalResponse>> findRandomAvailableAnimals() {
        List<AnimalResponse> randomAnimals = this.animalService.findRandomAvailableAnimals();
        return ResponseEntity.ok(randomAnimals);
    }

    @GetMapping("")
    public ResponseEntity<AnimalResponse> findAnimalById(@RequestParam Long animalId, @RequestParam Long userId) {
        try {
            AnimalResponse animal = this.animalService.findAnimalById(animalId, userId);
            return ResponseEntity.ok(animal);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal not found");
        }
    }

    @GetMapping("/available")
    public ResponseEntity<Page<AnimalWalkResponse>> findAllAvailableAnimals(@RequestParam int page, @RequestParam int size) {
        try {
            Page<AnimalWalkResponse> availableAnimals = this.animalService.findAllAvailableAnimals(page, size);
            return ResponseEntity.ok(availableAnimals);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
    }

    @PostMapping("/update-rating")
    public ResponseEntity<Page<AnimalWalkResponse>> updateAnimalRating(@RequestParam int page, @RequestParam int size) {
        try {
            Page<AnimalWalkResponse> updatedAnimals = this.animalService.findAllAvailableAnimals(page, size);
            return ResponseEntity.ok(updatedAnimals);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid rating update request");
        }
    }

    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Void> updateAnimal(
            @RequestParam Long id,
            @RequestParam("img") MultipartFile img,
            @ModelAttribute AnimalRequest animal
    ) {
        this.animalService.updateAnimal(id, animal, img);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Void> createAnimal(
            @RequestPart("img") MultipartFile img,
            @RequestPart("animal") AnimalRequest animal
    ) {
        this.animalService.createAnimal(animal, img);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAnimal(@RequestParam Long id) {
        this.animalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
