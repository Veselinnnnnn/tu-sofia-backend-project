package com.universityproject.backendproject.service.animal;

import com.universityproject.backendproject.model.dto.animal.request.AnimalRequest;
import com.universityproject.backendproject.model.dto.animal.response.AnimalAvailableResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalWalkResponse;
import com.universityproject.backendproject.model.entity.Animal;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.repository.AnimalRepository;
import com.universityproject.backendproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;


@Service
@AllArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public Page<AnimalResponse> findAll(int page) {

        PageRequest requestPage = PageRequest.of(page, 6);

        return animalRepository.findAll(requestPage)
                .map(a -> modelMapper.map(a, AnimalResponse.class));
    }

    @Override
    public void createAnimal(AnimalRequest animal) {
        Animal animalEntity = modelMapper.map(animal, Animal.class);
        animalEntity.setAvailability(true);

        animalRepository.save(animalEntity);
    }

    @Override
    public void adoptAnimal(Long id) {
        animalRepository.deleteById(id);
    }

    @Override
    public AnimalAvailableResponse findAnimalById(Long id) {
        try {
            Animal animal = animalRepository.findById(id).orElseThrow();
            return modelMapper.map(animal, AnimalAvailableResponse.class);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found", e);
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No accreditation !!!", e);
        }

    }

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
