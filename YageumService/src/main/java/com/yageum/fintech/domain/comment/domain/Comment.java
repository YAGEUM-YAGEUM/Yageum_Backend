package com.yageum.fintech.domain.comment.domain;

import com.yageum.fintech.domain.post.domain.Post;
import com.yageum.fintech.global.config.jwt.jwtInterceptor.JwtContextHolder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Comment {
    private final Long id;
    private final LocalDateTime createdAt;
    private final String content;
    private final Post post;
    private final LocalDateTime updatedAt;
    private final Long writeId;

    @Builder
    public Comment(Long id, LocalDateTime createdAt, String content, Post post, LocalDateTime updatedAt, Long writeId) {
        this.id = id;
        this.createdAt = createdAt;
        this.content = content;
        this.post = post;
        this.updatedAt = updatedAt;
        this.writeId = writeId;
    }

    public static Comment from(Post post, CommentCreate commentCreate) {
        return Comment.builder()
                .content(commentCreate.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .post(post)
                .writeId(JwtContextHolder.getUserId())
                .build();
    }

    public Comment update(CommentUpdate commentUpdate) {
        return Comment.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(LocalDateTime.now())
                .content(commentUpdate.getContent())
                .post(post)
                .writeId(writeId)
                .build();
    }

}
