package com.yageum.fintech.domain.post.model.dto.request;

import com.yageum.fintech.domain.post.domain.Post;
import com.yageum.fintech.domain.post.infrastructure.Category;

public record CreatePostRequestServiceDto(
	Long projectId,
	String title,
	String content,
	Category category
) {
	public Post toEntity(Long writerId) {
		return Post.builder()
			.title(title)
			.content(content)
			.category(category)
			.writerId(writerId)
			.build();
	}
}
