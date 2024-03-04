package com.yageum.fintech.domain.comment.controller.port;

import com.yageum.fintech.domain.comment.domain.Comment;
import com.yageum.fintech.domain.comment.domain.CommentCreate;
import com.yageum.fintech.domain.comment.domain.CommentUpdate;

import java.util.List;

public interface CommentService {
    Comment create(Long postId, CommentCreate commentCreate);

    List<Comment> read(Long postId);

    Comment update(Long commentId, CommentUpdate commentUpdate);

    void delete(Long commentId);
}
