package com.yageum.fintech.domain.post.model.dto.request;

import com.yageum.fintech.domain.post.infrastructure.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CreatePostRequestDto(
	@Positive(message = "projectId 필드의 값은 0 이상이어야 합니다.")
	@Schema(description = "프로젝트를 식별하는 식별자, 0 이하의 값 허용 x")
	Long projectId,

	@NotBlank(message = "title 필드는 공백을 허용하지 않습니다.")
	@Schema(description = "기획/제작/편집 게시글의 제목, null/\"\"(빈 공백)/\" \"(공백) 허용 x ")
	String title,

	@NotBlank(message = "content 필드는 공백을 허용하지 않습니다.")
	@Schema(description = "기획/제작/편집 게시글의 내용, null/\"\"(빈 공백)/\" \"(공백) 허용 x ")
	String content,

	@NotNull(message = "category 필드는 null 값을 허용하지 않습니다.")
	@Schema(description = "기획/제작/편집을 카테고리로 분류 (ENUM 타입), null 값 허용 x")
	Category category
) {
	public CreatePostRequestServiceDto toServiceRequest() {
		return new CreatePostRequestServiceDto( projectId, title, content, category);
	}
}
