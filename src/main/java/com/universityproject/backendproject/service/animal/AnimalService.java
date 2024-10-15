package com.universityproject.backendproject.service.animal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.universityproject.backendproject.model.dto.animal.request.AnimalRequest;
import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalWalkResponse;
import com.universityproject.backendproject.model.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnimalService {

    Page<AnimalWalkResponse> findAllAvailableAnimals(int page, int size);

    List<AnimalResponse> findAll();

    Page<AnimalResponse> findAllPaginated(int page, int size);

    AnimalResponse findAnimalById(Long id, Long userId);

    void updateAnimalRating(Long animalId);

    void updateAnimal(Long id, AnimalRequest animal, MultipartFile img) throws IOException;

    void createAnimal(AnimalRequest animal, MultipartFile img) throws Exception;

    void adoptAnimal(Long id);

//    AnimalAvailableResponse findAnimalById(Long id);
    void delete(Long id);

    List<AnimalResponse> findRandomAvailableAnimals();

    Page<AnimalWalkResponse> findAllAvailable(int page);

}
