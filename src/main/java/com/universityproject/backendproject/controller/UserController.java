package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.comment.request.CommentRequest;
import com.universityproject.backendproject.model.dto.comment.response.CommentResponse;
import com.universityproject.backendproject.model.dto.user.response.UserAvailableResponse;
import com.universityproject.backendproject.model.dto.walkhistory.response.WalkHistoryResponse;
import com.universityproject.backendproject.model.entity.WalkHistory;
import com.universityproject.backendproject.service.comment.CommentService;
import com.universityproject.backendproject.service.user.UserService;
import com.universityproject.backendproject.service.walkhistory.WalkHistoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final WalkHistoryService walkHistoryService;
    private final CommentService commentService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/available")
    public List<UserAvailableResponse> allAvailable() {
        return userService.findAllAvailable();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public List<UserAvailableResponse> findAllUsers() {
        try {
            return userService.findAllUsers();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);

        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/comments")
    public List<CommentResponse> getUserComments(@PathVariable Long id) {
        return commentService.findAllByUserId(id);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/{id}/walks/page/{page}")
    public Page<WalkHistoryResponse> getWalkHistory(@PathVariable Long id, @PathVariable int page) throws ParseException {
        try {
            Page<WalkHistory> walkHistories = walkHistoryService.findByUserId(id, page);

            return walkHistories
                    .map(walkHistory -> modelMapper.map(walkHistory, WalkHistoryResponse.class));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);

        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/animal/{animalId}/walk")
    public void takeOnWalk(@RequestParam Long id, @PathVariable Long animalId) {
        try {
            userService.takeOnWalk(id, animalId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/animal/{animalId}/return")
    public void returnFromWalk(@PathVariable Long id, @PathVariable Long animalId) {

        userService.returnFromWalk(id, animalId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/comment")
    public void addComment(@RequestBody CommentRequest commentRequest) {
        commentService.createComment(commentRequest.getAuthorId(),
                commentRequest.getDescription(),
                commentRequest.getUserId());
    }
}
