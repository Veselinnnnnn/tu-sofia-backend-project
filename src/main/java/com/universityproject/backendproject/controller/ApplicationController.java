package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.application.request.ApplicationCompositeRequest;
import com.universityproject.backendproject.model.dto.application.response.ApplicationResponse;
import com.universityproject.backendproject.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(@RequestBody ApplicationCompositeRequest request) {
        ApplicationResponse response = this.applicationService.create(request);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        this.applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestParam Long id, @RequestBody ApplicationCompositeRequest request) {
        this.applicationService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> findById(@PathVariable Long id) {
        ApplicationResponse response = this.applicationService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<List<ApplicationResponse>> getRequestsByUserId(@RequestParam Long userId) {
        List<ApplicationResponse> responses = this.applicationService.getApplicationsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApplicationResponse> approveApplication(@PathVariable Long id) {
        ApplicationResponse response = this.applicationService.approveApplication(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<ApplicationResponse> declineApplication(@PathVariable Long id) {
        ApplicationResponse response = this.applicationService.declineApplication(id);
        return ResponseEntity.ok(response);
    }
}
