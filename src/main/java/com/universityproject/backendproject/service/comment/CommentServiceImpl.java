package com.universityproject.backendproject.service.comment;

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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentReactionRepository commentReactionRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final AnimalService animalService;

    @Override
    public void addComment(Long animalId, Long userId, CommentRequest request) {
        Animal animal = animalRepository.findById(animalId).orElseThrow(() -> new IllegalArgumentException("Invalid animal ID"));
        User user = this.userRepository.findUserById(userId);

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setRating(request.getRating());
        comment.setAnimal(animal);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        this.animalService.updateAnimalRating(comment.getAnimal().getId());
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            commentRepository.delete(comment);
            animalService.updateAnimalRating(comment.getAnimal().getId());
        }
    }

    @Override
    public void updateComment(Long commentId, Comment request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        comment.setContent(request.getContent());
        comment.setRating(request.getRating());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        this.animalService.updateAnimalRating(comment.getAnimal().getId());
    }


    @Override
    public CommentResponse incrementLikes(Long commentId, Long userId) {
        return handleReaction(commentId, userId, ReactionTypeEnum.LIKE);
    }

    @Override
    public CommentResponse incrementDislikes(Long commentId, Long userId) {
        return handleReaction(commentId, userId, ReactionTypeEnum.DISLIKE);
    }

//    @Override
//    public CommentResponse incrementLikes(Long id) {
//        Comment comment = commentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Comment not found"));
//        comment.setLikes(comment.getLikes() + 1);
//        Comment updatedComment = commentRepository.save(comment);
//        return modelMapper.map(updatedComment, CommentResponse.class);
//    }
//
//    @Override
//    public CommentResponse incrementDislikes(Long id) {
//        Comment comment = commentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Comment not found"));
//        comment.setDislikes(comment.getDislikes() + 1);
//        Comment updatedComment = commentRepository.save(comment);
//        return modelMapper.map(updatedComment, CommentResponse.class);
//    }


//    @Override
//    public List<CommentResponse> findCommentsByAnimalId(Long animalId) {
//        List<Comment> comments = this.commentRepository.findAllByAnimalId(animalId);
//
//        return comments.stream()
//                .map(comment -> modelMapper.map(comment, CommentResponse.class))
//                .collect(Collectors.toList());
//
//    }

    @Override
    public List<CommentResponse> findCommentsByAnimalId(Long animalId) {
        List<Comment> comments = this.commentRepository.findAllByAnimalId(animalId);

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
        Optional<CommentReaction> existingReaction = commentReactionRepository.findByUserIdAndCommentId(userId, commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        CommentReaction userReaction = null;

        if (existingReaction.isPresent()) {
            CommentReaction reaction = existingReaction.get();

            // If the reaction is the same as the existing one, return without making changes
            if (reaction.getReactionType() == reactionType) {
                CommentResponse response = modelMapper.map(comment, CommentResponse.class);
                response.setCurrentUserReactionType(reaction.getReactionType());
                return response;
            } else {
                // Remove the previous reaction count
                if (reaction.getReactionType() == ReactionTypeEnum.LIKE) {
                    comment.setLikes(comment.getLikes() - 1);
                } else if (reaction.getReactionType() == ReactionTypeEnum.DISLIKE) {
                    comment.setDislikes(comment.getDislikes() - 1);
                }

                // Update to the new reaction type
                reaction.setReactionType(reactionType);
                userReaction = commentReactionRepository.save(reaction);
            }
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            CommentReaction reaction = new CommentReaction();
            reaction.setUser(user);
            reaction.setComment(comment);
            reaction.setReactionType(reactionType);
            userReaction = commentReactionRepository.save(reaction);
        }

        // Add the new reaction count
        if (reactionType == ReactionTypeEnum.LIKE) {
            comment.setLikes(comment.getLikes() + 1);
        } else if (reactionType == ReactionTypeEnum.DISLIKE) {
            comment.setDislikes(comment.getDislikes() + 1);
        }

        Comment updatedComment = commentRepository.save(comment);

        // Map the updated comment and set the user reaction in the response
        CommentResponse response = modelMapper.map(updatedComment, CommentResponse.class);
        response.setCurrentUserReactionType(userReaction.getReactionType());
        return response;
    }


//    @Override
//    public void createComment(Long authorId, String description, Long userId) {
//        if (userRepository.findById(authorId).isPresent() && userRepository.findById(userId).isPresent()) {
//
//            User author = userRepository.findById(authorId).get();
//            User user = userRepository.findById(userId).get();
//
//            Comment comment = new Comment(description, author.getUsername(), user);
//
//            commentRepository.save(comment);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found.");
//        }
//
//    }

//    @Override
//    public List<CommentResponse> findAllByUserId(Long id) {
//        try {
//            List<Comment> comments = commentRepository.findAllByUserId(id);
//
//            if (comments.size() > 5) {
//                comments = comments.subList(comments.size() - 5, comments.size());
//            }
//
//            Collections.reverse(comments);
//
//            return comments.stream()
//                    .map(c -> modelMapper.map(c, CommentResponse.class))
//                    .collect(Collectors.toList());
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no comments", e);
//        }
//    }
}
