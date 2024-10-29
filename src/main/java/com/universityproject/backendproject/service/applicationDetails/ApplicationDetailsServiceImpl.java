package com.universityproject.backendproject.service.applicationDetails;

import com.universityproject.backendproject.exception.ApplicationDetailsNotFoundException;
import com.universityproject.backendproject.model.dto.application.response.ApplicationDetailsResponse;
import com.universityproject.backendproject.model.entity.ApplicationDetails;
import com.universityproject.backendproject.repository.ApplicationDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicationDetailsServiceImpl implements ApplicationDetailsService {

    private final ApplicationDetailsRepository applicationDetailsRepository;
    private final ModelMapper modelMapper; // If you want to use ModelMapper for mapping

    @Override
    public ApplicationDetailsResponse getApplicationDetailsByApplicationId(Long applicationId) {
        log.debug("Fetching application details for application ID: {}", applicationId);
        Optional<ApplicationDetails> applicationDetailsOpt = this.applicationDetailsRepository.findByApplicationId(applicationId);

        ApplicationDetails applicationDetails = applicationDetailsOpt
                .orElseThrow(() -> new ApplicationDetailsNotFoundException("Application details not found for application ID: " + applicationId));

        return this.mapToResponse(applicationDetails);
    }

    @Override
    public void delete(Long id) {
        log.debug("Attempting to delete application details with ID: {}", id);
        if (!this.applicationDetailsRepository.existsById(id)) {
            log.error("Application details not found with ID: {}", id);
            throw new ApplicationDetailsNotFoundException("Application details not found with ID: " + id);
        }
        this.applicationDetailsRepository.deleteById(id);
        log.info("Deleted application details with ID: {}", id);
    }

    private ApplicationDetailsResponse mapToResponse(ApplicationDetails applicationDetails) {
        log.debug("Mapping ApplicationDetails to ApplicationDetailsResponse for ID: {}", applicationDetails.getId());
        ApplicationDetailsResponse response = modelMapper.map(applicationDetails, ApplicationDetailsResponse.class);

        response.setPickUpTime(applicationDetails.getPickUpTime() != null ? applicationDetails.getPickUpTime().toString() : null);
        response.setReturnTime(applicationDetails.getReturnTime() != null ? applicationDetails.getReturnTime().toString() : null);

        return response;
    }
}
