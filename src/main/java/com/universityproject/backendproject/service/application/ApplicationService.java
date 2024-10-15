package com.universityproject.backendproject.service.application;

import com.universityproject.backendproject.model.dto.application.request.ApplicationCompositeRequest;
import com.universityproject.backendproject.model.dto.application.request.ApplicationDetailsRequest;
import com.universityproject.backendproject.model.dto.application.request.ApplicationRequest;
import com.universityproject.backendproject.model.dto.application.response.ApplicationResponse;
import jakarta.mail.MessagingException;

import java.util.List;

public interface ApplicationService {
//    ApplicationResponse create(ApplicationRequest request) throws MessagingException;
    ApplicationResponse create(ApplicationCompositeRequest request) throws MessagingException;

    void delete(Long applicationId);

    List<ApplicationResponse> getApplicationByEmail(String email);

    List<ApplicationResponse> getApplicationsByUserId(Long userId);

    void update(Long applicationId, ApplicationCompositeRequest request) throws MessagingException;
//    ApplicationResponse update(Long applicationId, ApplicationRequest request) throws MessagingException;

    ApplicationResponse declineApplication(Long applicationId) throws MessagingException;

    ApplicationResponse approveApplication(Long applicationId) throws MessagingException;

    ApplicationResponse findById(Long applicationId);

    void updateStatuses();
}
