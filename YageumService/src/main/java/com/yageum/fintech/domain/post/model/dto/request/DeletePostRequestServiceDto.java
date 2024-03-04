package com.yageum.fintech.domain.post.model.dto.request;

public record DeletePostRequestServiceDto(
	Long projectId,
	Long postId
) {
}
