package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.testimonial.request.TestimonialRequest;
import com.universityproject.backendproject.model.dto.testimonial.response.TestimonialResponse;
import com.universityproject.backendproject.service.testiomonial.TestimonialService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testimonials")
@AllArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;

    @GetMapping
    public ResponseEntity<List<TestimonialResponse>> getAllTestimonials() {
        List<TestimonialResponse> testimonials = testimonialService.getAllTestimonials();
        return ResponseEntity.ok(testimonials);
    }

    @PostMapping
    public ResponseEntity<TestimonialResponse> submitTestimonial(@RequestBody TestimonialRequest request) {
        TestimonialResponse testimonialResponse = testimonialService.saveTestimonial(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(testimonialResponse);
    }

    @PutMapping()
    public ResponseEntity<TestimonialResponse> updateTestimonial(@RequestParam Long id, @RequestBody TestimonialRequest testimonialRequest) {
        TestimonialResponse updatedTestimonial = testimonialService.updateTestimonial(id, testimonialRequest);
        return ResponseEntity.ok(updatedTestimonial);
    }

    @DeleteMapping()
    public  ResponseEntity<Void> deleteTestimonial(@RequestParam Long id) {
        testimonialService.deleteTestimonial(id);
        return ResponseEntity.noContent().build();
    }
}
