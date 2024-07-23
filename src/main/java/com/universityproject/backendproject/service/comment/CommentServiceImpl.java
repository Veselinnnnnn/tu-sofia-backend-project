package com.universityproject.backendproject.service.comment;

import com.universityproject.backendproject.model.dto.comment.response.CommentResponse;
import com.universityproject.backendproject.model.entity.Comment;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.repository.CommentRepository;
import com.universityproject.backendproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createComment(Long authorId, String description, Long userId) {
        if (userRepository.findById(authorId).isPresent() && userRepository.findById(userId).isPresent()) {

            User author = userRepository.findById(authorId).get();
            User user = userRepository.findById(userId).get();

            Comment comment = new Comment(description, author.getUsername(), user);

            commentRepository.save(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found.");
        }

    }

    @Override
    public List<CommentResponse> findAllByUserId(Long id) {
        try {
            List<Comment> comments = commentRepository.findAllByUserId(id);

            if (comments.size() > 5) {
                comments = comments.subList(comments.size() - 5, comments.size());
            }

            Collections.reverse(comments);

            return comments.stream()
                    .map(c -> modelMapper.map(c, CommentResponse.class))
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no comments", e);
        }
    }
}
