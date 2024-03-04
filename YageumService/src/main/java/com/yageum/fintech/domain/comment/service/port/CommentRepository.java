package com.yageum.fintech.domain.comment.service.port;

import com.yageum.fintech.domain.comment.domain.Comment;
import com.yageum.fintech.domain.post.domain.Post;

import java.util.Optional;

public interface CommentRepository {

    Optional<Comment> findById(Long commentId);

    Comment save(Comment comment);

    void delete(Comment comment);

    void deleteAll(Post post);
}
