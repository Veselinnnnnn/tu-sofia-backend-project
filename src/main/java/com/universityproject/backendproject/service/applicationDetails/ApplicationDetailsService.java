package com.universityproject.backendproject.service.applicationDetails;

import com.universityproject.backendproject.model.dto.application.request.ApplicationDetailsRequest;
import com.universityproject.backendproject.model.dto.application.request.ApplicationRequest;
import com.universityproject.backendproject.model.dto.application.response.ApplicationDetailsResponse;
import com.universityproject.backendproject.model.dto.application.response.ApplicationResponse;
import com.universityproject.backendproject.model.entity.ApplicationDetails;
import jakarta.mail.MessagingException;

import java.util.List;

public interface ApplicationDetailsService {
    ApplicationDetailsResponse getApplicationDetailsByApplicationId(Long applicationId);

    void delete(Long id);
}
