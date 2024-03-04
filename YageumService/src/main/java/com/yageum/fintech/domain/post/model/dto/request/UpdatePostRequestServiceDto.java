package com.yageum.fintech.domain.post.model.dto.request;

import com.yageum.fintech.domain.post.infrastructure.Category;

public record UpdatePostRequestServiceDto(
	Long projectId,
	Long postId,
	String title,
	String content,
	Category category

) {
}
