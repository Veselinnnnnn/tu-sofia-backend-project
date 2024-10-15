package com.universityproject.backendproject.service.comment;

import com.universityproject.backendproject.exception.AnimalNotFoundException;
import com.universityproject.backendproject.exception.CommentNotFoundException;
import com.universityproject.backendproject.exception.UserNotFoundException;
import com.universityproject.backendproject.model.dto.comment.request.CommentRequest;
import com.universityproject.backendproject.model.dto.comment.response.CommentResponse;
import com.universityproject.backendproject.model.entity.Animal;
import com.universityproject.backendproject.model.entity.Comment;
import com.universityproject.backendproject.model.entity.CommentReaction;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.model.enums.ReactionTypeEnum;
import com.universityproject.backendproject.repository.AnimalRepository;
import com.universityproject.backendproject.repository.CommentReactionRepository;
import com.universityproject.backendproject.repository.CommentRepository;
import com.universityproject.backendproject.repository.UserRepository;
import com.universityproject.backendproject.service.animal.AnimalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentReactionRepository commentReactionRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AnimalService animalService;

    @Override
    public void addComment(Long animalId, Long userId, CommentRequest request) {
        log.info("Adding comment for animal ID: {}, user ID: {}", animalId, userId);

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException("Invalid animal ID: " + animalId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Invalid user ID: " + userId));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setRating(request.getRating());
        comment.setAnimal(animal);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
        animalService.updateAnimalRating(comment.getAnimal().getId());
        log.info("Comment added successfully for animal ID: {}", animalId);
    }

    @Override
    public void deleteComment(Long id) {
        log.info("Deleting comment ID: {}", id);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + id));

        commentRepository.delete(comment);
        animalService.updateAnimalRating(comment.getAnimal().getId());
        log.info("Comment deleted successfully with ID: {}", id);
    }

    @Override
    public void updateComment(Long commentId, Comment request) {
        log.info("Updating comment ID: {}", commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        comment.setContent(request.getContent());
        comment.setRating(request.getRating());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
        animalService.updateAnimalRating(comment.getAnimal().getId());
        log.info("Comment updated successfully with ID: {}", commentId);
    }

    @Override
    public CommentResponse incrementLikes(Long commentId, Long userId) {
        log.info("Incrementing likes for comment ID: {}, user ID: {}", commentId, userId);
        return handleReaction(commentId, userId, ReactionTypeEnum.LIKE);
    }

    @Override
    public CommentResponse incrementDislikes(Long commentId, Long userId) {
        log.info("Incrementing dislikes for comment ID: {}, user ID: {}", commentId, userId);
        return handleReaction(commentId, userId, ReactionTypeEnum.DISLIKE);
    }

    @Override
    public List<CommentResponse> findCommentsByAnimalId(Long animalId) {
        log.info("Finding comments for animal ID: {}", animalId);

        List<Comment> comments = commentRepository.findAllByAnimalId(animalId);

        return comments.stream()
                .map(comment -> {
                    CommentResponse response = modelMapper.map(comment, CommentResponse.class);
                    response.setUsername(comment.getUser().getUsername());
                    response.setLikes(comment.getLikes());
                    response.setDislikes(comment.getDislikes());
                    response.setRating(comment.getRating());
                    return response;
                })
                .collect(Collectors.toList());
    }

    private CommentResponse handleReaction(Long commentId, Long userId, ReactionTypeEnum reactionType) {
        log.info("Handling reaction for comment ID: {}, user ID: {}, reaction type: {}", commentId, userId, reactionType);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        Optional<CommentReaction> existingReaction = commentReactionRepository.findByUserIdAndCommentId(userId, commentId);
        CommentReaction userReaction;

        if (existingReaction.isPresent()) {
            CommentReaction reaction = existingReaction.get();
            if (reaction.getReactionType() == reactionType) {
                log.info("User has already reacted with the same type: {}", reactionType);
                return prepareResponse(comment, reaction.getReactionType());
            } else {
                updateReactionCounts(comment, reaction.getReactionType(), -1);
                reaction.setReactionType(reactionType);
                userReaction = commentReactionRepository.save(reaction);
            }
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

            userReaction = new CommentReaction();
            userReaction.setUser(user);
            userReaction.setComment(comment);
            userReaction.setReactionType(reactionType);
            userReaction = commentReactionRepository.save(userReaction);
        }

        updateReactionCounts(comment, reactionType, 1);
        Comment updatedComment = commentRepository.save(comment);

        return prepareResponse(updatedComment, userReaction.getReactionType());
    }

    private void updateReactionCounts(Comment comment, ReactionTypeEnum reactionType, int increment) {
        if (reactionType == ReactionTypeEnum.LIKE) {
            comment.setLikes(comment.getLikes() + increment);
        } else if (reactionType == ReactionTypeEnum.DISLIKE) {
            comment.setDislikes(comment.getDislikes() + increment);
        }
    }

    private CommentResponse prepareResponse(Comment comment, ReactionTypeEnum currentUserReactionType) {
        CommentResponse response = modelMapper.map(comment, CommentResponse.class);
        response.setCurrentUserReactionType(currentUserReactionType);
        return response;
    }
}
