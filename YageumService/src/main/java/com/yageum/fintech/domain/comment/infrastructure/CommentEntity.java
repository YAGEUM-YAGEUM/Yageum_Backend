package com.yageum.fintech.domain.comment.infrastructure;

import com.yageum.fintech.domain.comment.domain.Comment;
import com.yageum.fintech.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_index")
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_index")
    private Post post;

    private Long writeId;

    public static CommentEntity from(Comment comment) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.id = comment.getId();
        commentEntity.content = comment.getContent();
        commentEntity.createdAt = comment.getCreatedAt();
        commentEntity.post = comment.getPost();
        commentEntity.updatedAt = comment.getUpdatedAt();
        commentEntity.writeId = comment.getWriteId();
        return commentEntity;
    }

    public Comment toModel(){
        return Comment.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .content(content)
                .post(post)
                .writeId(writeId)
                .build();
    }

}
