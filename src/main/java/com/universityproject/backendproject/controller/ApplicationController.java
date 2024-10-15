package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.application.request.ApplicationCompositeRequest;
import com.universityproject.backendproject.model.dto.application.request.ApplicationDetailsRequest;
import com.universityproject.backendproject.model.dto.application.request.ApplicationRequest;
import com.universityproject.backendproject.model.dto.application.response.ApplicationResponse;
import com.universityproject.backendproject.service.application.ApplicationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

//    @PostMapping
//    public ApplicationResponse create(@RequestBody ApplicationRequest request) throws MessagingException {
//        return this.applicationService.create(request);
//    }

    @PostMapping
    public ApplicationResponse create(@RequestBody ApplicationCompositeRequest request) throws MessagingException {
        return applicationService.create(request);
    }

    @DeleteMapping
    public void delete(@RequestParam Long id) {
        this.applicationService.delete(id);
    }

//    @PutMapping()
//    public ApplicationResponse update(
//            @RequestParam Long applicationId,
//            @RequestBody ApplicationRequest request) throws MessagingException {
//        return applicationService.update(applicationId, request);
//    }

    @PutMapping()
    public void update(@RequestParam Long id, @RequestBody ApplicationCompositeRequest request) throws MessagingException {
            this.applicationService.update(id, request);
    }

    @GetMapping("/{id}")
    public ApplicationResponse findById(@PathVariable Long id) {
        return this.applicationService.findById(id);
    }

    @GetMapping("")
    public List<ApplicationResponse> getRequestsByUserId(@RequestParam Long userId) {
        return this.applicationService.getApplicationsByUserId(userId);
    }

    @PostMapping("/{id}/approve")
    public ApplicationResponse approveApplication(@PathVariable Long id) throws MessagingException {
        return this.applicationService.approveApplication(id);
    }

    @PostMapping("/{id}/decline")
    public ApplicationResponse declineApplication(@PathVariable Long id) throws MessagingException {
        return this.applicationService.declineApplication(id);
    }

//    @GetMapping("/{applicationId}")
//    public ApplicationResponse getApplicationById(@PathVariable Long applicationId) {
//        return applicationService.getApplicationById(applicationId);
//    }
}
