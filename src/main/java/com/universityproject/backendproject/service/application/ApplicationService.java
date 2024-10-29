package com.universityproject.backendproject.service.application;

import com.universityproject.backendproject.model.dto.application.request.ApplicationCompositeRequest;
import com.universityproject.backendproject.model.dto.application.response.ApplicationResponse;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse create(ApplicationCompositeRequest request);

    void delete(Long applicationId);

    List<ApplicationResponse> getApplicationsByUserId(Long userId);

    void update(Long applicationId, ApplicationCompositeRequest request);

    ApplicationResponse declineApplication(Long applicationId);

    ApplicationResponse approveApplication(Long applicationId);

    ApplicationResponse findById(Long applicationId);

    void updateStatuses();
}
