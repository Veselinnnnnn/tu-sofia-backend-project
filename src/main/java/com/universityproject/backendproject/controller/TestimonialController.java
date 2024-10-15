package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.testimonial.request.TestimonialRequest;
import com.universityproject.backendproject.model.dto.testimonial.response.TestimonialResponse;
import com.universityproject.backendproject.service.testiomonial.TestimonialService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testimonials")
@AllArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;

    @GetMapping()
    public List<TestimonialResponse> getAllTestimonials() {
        return testimonialService.getAllTestimonials();
    }

    @PostMapping()
    public TestimonialResponse submitTestimonial(@RequestBody TestimonialRequest request) {
        return testimonialService.saveTestimonial(request);
    }

    @PutMapping()
    public TestimonialResponse updateTestimonial(@RequestParam Long id, @RequestBody TestimonialRequest testimonialRequest) {
        return testimonialService.updateTestimonial(id, testimonialRequest);
    }

    @DeleteMapping()
    public void deleteTestimonial(@RequestParam Long id) {
        testimonialService.deleteTestimonial(id);
    }
}
