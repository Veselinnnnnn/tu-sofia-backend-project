package com.universityproject.backendproject.service.applicationDetails;

import com.universityproject.backendproject.model.dto.application.request.ApplicationDetailsRequest;
import com.universityproject.backendproject.model.dto.application.response.ApplicationDetailsResponse;
import com.universityproject.backendproject.model.entity.Application;
import com.universityproject.backendproject.model.entity.ApplicationDetails;
import com.universityproject.backendproject.repository.ApplicationDetailsRepository;
import com.universityproject.backendproject.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplicationDetailsServiceImpl implements ApplicationDetailsService {

    private final ApplicationDetailsRepository applicationDetailsRepository;

    public ApplicationDetailsResponse getApplicationDetailsByApplicationId(Long applicationId) {
        Optional<ApplicationDetails> applicationDetailsOpt = this.applicationDetailsRepository.findByApplicationId(applicationId);
        return applicationDetailsOpt.map(this::mapToResponse).orElse(null);
    }

    @Override
    public void delete(Long id) {
        if (!applicationDetailsRepository.existsById(id)) {
            throw new IllegalArgumentException("Application details not found with ID: " + id);
        }
        applicationDetailsRepository.deleteById(id);
    }

    private ApplicationDetailsResponse mapToResponse(ApplicationDetails applicationDetails) {
        ApplicationDetailsResponse response = new ApplicationDetailsResponse();
        response.setId(applicationDetails.getId());
        response.setApplicationId(applicationDetails.getApplication().getId());
        response.setFirstName(applicationDetails.getFirstName());
        response.setLastName(applicationDetails.getLastName());
        response.setEmail(applicationDetails.getEmail());
        response.setPhoneNumber(applicationDetails.getPhoneNumber());
        response.setAddress(applicationDetails.getAddress());
        response.setCity(applicationDetails.getCity());
        response.setState(applicationDetails.getState());
        response.setPostalCode(applicationDetails.getPostalCode());
        response.setCountry(applicationDetails.getCountry());
        response.setReasonForAdoption(applicationDetails.getReasonForAdoption());
        response.setAnimalId(applicationDetails.getAnimal().getId());
        response.setPickUpTime(applicationDetails.getPickUpTime() != null ? applicationDetails.getPickUpTime().toString() : null);
        response.setReturnTime(applicationDetails.getReturnTime() != null ? applicationDetails.getReturnTime().toString() : null);
        response.setHasPreviousExperienceWithPets(applicationDetails.getHasPreviousExperienceWithPets());
        response.setHasOtherPets(applicationDetails.getHasOtherPets());
        response.setHasChildren(applicationDetails.getHasChildren());
        response.setHasFencedYard(applicationDetails.getHasFencedYard());
        response.setReferenceContact(applicationDetails.getReferenceContact());
        response.setBackgroundCheckStatus(applicationDetails.getBackgroundCheckStatus());

        return response;
    }
}

