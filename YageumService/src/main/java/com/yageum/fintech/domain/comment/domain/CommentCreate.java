package com.yageum.fintech.domain.comment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentCreate {
    private final String content;

    @Builder
    public CommentCreate(
            @JsonProperty("content") String content) {
        this.content = content;
    }
}
