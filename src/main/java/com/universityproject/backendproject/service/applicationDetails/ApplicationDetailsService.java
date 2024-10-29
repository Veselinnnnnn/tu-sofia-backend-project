package com.universityproject.backendproject.service.applicationDetails;

import com.universityproject.backendproject.model.dto.application.response.ApplicationDetailsResponse;

public interface ApplicationDetailsService {
    ApplicationDetailsResponse getApplicationDetailsByApplicationId(Long applicationId);

    void delete(Long id);
}
