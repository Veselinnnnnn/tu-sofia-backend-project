package com.universityproject.backendproject.service.animal;

import com.universityproject.backendproject.model.dto.animal.request.AnimalRequest;
import com.universityproject.backendproject.model.dto.animal.response.AnimalAvailableResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalWalkResponse;
import org.springframework.data.domain.Page;

public interface AnimalService {

    Page<AnimalResponse> findAll(int page);

    void createAnimal(AnimalRequest animal) throws Exception;

    void adoptAnimal(Long id);

    AnimalAvailableResponse findAnimalById(Long id);


    Page<AnimalWalkResponse> findAllAvailable(int page);

}
