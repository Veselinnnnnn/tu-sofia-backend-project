package com.universityproject.backendproject.model.entity;


import com.universityproject.backendproject.model.enums.ReactionTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_reactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Enumerated(EnumType.STRING)
    private ReactionTypeEnum reactionType;
}
