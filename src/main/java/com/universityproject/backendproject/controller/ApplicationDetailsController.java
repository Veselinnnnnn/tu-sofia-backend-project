package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.application.response.ApplicationDetailsResponse;
import com.universityproject.backendproject.service.applicationDetails.ApplicationDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application-details")
@RequiredArgsConstructor
public class ApplicationDetailsController {

    private final ApplicationDetailsService applicationDetailsService;

    @GetMapping()
    public ResponseEntity<ApplicationDetailsResponse> getApplicationDetails(@RequestParam Long applicationId) {
        ApplicationDetailsResponse response = this.applicationDetailsService.getApplicationDetailsByApplicationId(applicationId);
        return ResponseEntity.ok(response);
    }
}
