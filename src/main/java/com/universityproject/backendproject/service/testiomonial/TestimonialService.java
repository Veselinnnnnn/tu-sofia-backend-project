package com.universityproject.backendproject.service.testiomonial;

import com.universityproject.backendproject.model.dto.testimonial.request.TestimonialRequest;
import com.universityproject.backendproject.model.dto.testimonial.response.TestimonialResponse;

import java.util.List;

public interface TestimonialService {
    List<TestimonialResponse> getAllTestimonials();

    TestimonialResponse saveTestimonial(TestimonialRequest request);

    TestimonialResponse updateTestimonial(Long id, TestimonialRequest request);

    void deleteTestimonial(Long testimonialId);
}
