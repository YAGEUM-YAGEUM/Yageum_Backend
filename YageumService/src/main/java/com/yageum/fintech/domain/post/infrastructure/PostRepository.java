package com.yageum.fintech.domain.post.infrastructure;

import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.domain.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategoryAndProject(Category getCategory, Project project);

    List<Post> findByCategoryAndProject(Category getCategory, Project project, Pageable pageable);

}
