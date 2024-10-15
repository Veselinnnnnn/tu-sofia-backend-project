package com.universityproject.backendproject.service.testiomonial;

import com.universityproject.backendproject.model.dto.testimonial.request.TestimonialRequest;
import com.universityproject.backendproject.model.dto.testimonial.response.TestimonialResponse;
import com.universityproject.backendproject.model.entity.Testimonial;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.repository.TestimonialRepository;
import com.universityproject.backendproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestimonialServiceImpl implements TestimonialService {

    private final TestimonialRepository testimonialRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public List<TestimonialResponse> getAllTestimonials() {
        return testimonialRepository.findAll().stream()
                .map(testimonial -> modelMapper.map(testimonial, TestimonialResponse.class))
                .collect(Collectors.toList());
    }

    public TestimonialResponse saveTestimonial(TestimonialRequest testimonialRequest) {
        // Find the user by ID
        User user = userRepository.findById(testimonialRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")); // Handle this as needed

        Testimonial testimonial = Testimonial.builder()
                .message(testimonialRequest.getMessage())
                .createdAt(new Date()) // Set the current timestamp
                .user(user) // Set the associated user
                .build();

        Testimonial savedTestimonial = testimonialRepository.save(testimonial);
        return modelMapper.map(savedTestimonial, TestimonialResponse.class);
    }

    public TestimonialResponse updateTestimonial(Long id, TestimonialRequest testimonialRequest) {
        Optional<Testimonial> existingTestimonialOpt = testimonialRepository.findById(id);
        if (existingTestimonialOpt.isPresent()) {
            Testimonial existingTestimonial = existingTestimonialOpt.get();
            // Find the user by ID
            User user = userRepository.findById(testimonialRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found")); // Handle this as needed

            // Update the fields of the existing testimonial
            existingTestimonial.setMessage(testimonialRequest.getMessage());
            existingTestimonial.setUser(user); // Update the associated user

            Testimonial updatedTestimonial = testimonialRepository.save(existingTestimonial);
            return modelMapper.map(updatedTestimonial, TestimonialResponse.class);
        } else {
            // Handle the case where the testimonial doesn't exist
            return null; // Or throw an exception
        }
    }

    public void deleteTestimonial(Long testimonialId) {
        testimonialRepository.deleteById(testimonialId);
    }
}
