package com.yageum.fintech.domain.comment.infrastructure;

import com.yageum.fintech.domain.comment.domain.Comment;
import com.yageum.fintech.domain.comment.service.port.CommentRepository;
import com.yageum.fintech.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentJpaRepository.findById(commentId).map(CommentEntity::toModel);
    }

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(CommentEntity.from(comment)).toModel();
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(CommentEntity.from(comment));
    }

    @Override
    public void deleteAll(Post post) {
//        List<Comment> commentList = commentJpaRepository.findByPost(post);
//        for(CommentEntity comment : commentList){
//
//        }
    }


    public List<Comment> findByPost(Post post){
        return null;
    }
}
