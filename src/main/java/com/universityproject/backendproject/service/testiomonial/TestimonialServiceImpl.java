package com.universityproject.backendproject.service.testiomonial;

import com.universityproject.backendproject.exception.TestimonialNotFoundException;
import com.universityproject.backendproject.exception.UserNotFoundException;
import com.universityproject.backendproject.model.dto.testimonial.request.TestimonialRequest;
import com.universityproject.backendproject.model.dto.testimonial.response.TestimonialResponse;
import com.universityproject.backendproject.model.entity.Testimonial;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.repository.TestimonialRepository;
import com.universityproject.backendproject.repository.UserRepository;
import com.universityproject.backendproject.service.jwt.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestimonialServiceImpl implements TestimonialService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

    private final TestimonialRepository testimonialRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<TestimonialResponse> getAllTestimonials() {
        log.info("Fetching all testimonials");
        return testimonialRepository.findAll().stream()
                .map(testimonial -> modelMapper.map(testimonial, TestimonialResponse.class))
                .collect(Collectors.toList());
    }

    public TestimonialResponse saveTestimonial(TestimonialRequest testimonialRequest) {
        User user = findUserById(testimonialRequest.getUserId());

        Testimonial testimonial = Testimonial.builder()
                .message(testimonialRequest.getMessage())
                .createdAt(new Date())
                .user(user)
                .build();

        Testimonial savedTestimonial = testimonialRepository.save(testimonial);
        log.info("Saved testimonial with ID: {}", savedTestimonial.getId());
        return modelMapper.map(savedTestimonial, TestimonialResponse.class);
    }

    public TestimonialResponse updateTestimonial(Long id, TestimonialRequest testimonialRequest) {
        log.info("Updating testimonial with ID: {}", id);
        Testimonial existingTestimonial = testimonialRepository.findById(id)
                .orElseThrow(() -> new TestimonialNotFoundException("Testimonial not found with ID: " + id));

        User user = findUserById(testimonialRequest.getUserId());

        existingTestimonial.setMessage(testimonialRequest.getMessage());
        existingTestimonial.setUser(user);

        Testimonial updatedTestimonial = testimonialRepository.save(existingTestimonial);
        log.info("Updated testimonial with ID: {}", updatedTestimonial.getId());
        return modelMapper.map(updatedTestimonial, TestimonialResponse.class);
    }

    public void deleteTestimonial(Long testimonialId) {
        log.info("Deleting testimonial with ID: {}", testimonialId);
        if (!testimonialRepository.existsById(testimonialId)) {
            log.error("Attempted to delete non-existent testimonial with ID: {}", testimonialId);
            throw new TestimonialNotFoundException("Testimonial not found with ID: " + testimonialId);
        }
        testimonialRepository.deleteById(testimonialId);
        log.info("Deleted testimonial with ID: {}", testimonialId);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }
}
