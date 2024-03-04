package com.yageum.fintech.domain.comment.infrastructure;

import com.yageum.fintech.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPost(Post post);
}
