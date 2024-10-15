package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.application.request.ApplicationCompositeRequest;
import com.universityproject.backendproject.model.dto.application.request.ApplicationRequest;
import com.universityproject.backendproject.model.dto.application.response.ApplicationDetailsResponse;
import com.universityproject.backendproject.model.dto.application.response.ApplicationResponse;
import com.universityproject.backendproject.service.application.ApplicationService;
import com.universityproject.backendproject.service.applicationDetails.ApplicationDetailsService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.List;

@RestController
@RequestMapping("/application-details")
@RequiredArgsConstructor
public class ApplicationDetailsController {

    private final ApplicationDetailsService applicationDetailsService;

    @GetMapping()
    public ApplicationDetailsResponse getApplicationDetails(@RequestParam Long applicationId) {
        System.out.println(applicationDetailsService.getApplicationDetailsByApplicationId(applicationId));
        return applicationDetailsService.getApplicationDetailsByApplicationId(applicationId);
    }
}
