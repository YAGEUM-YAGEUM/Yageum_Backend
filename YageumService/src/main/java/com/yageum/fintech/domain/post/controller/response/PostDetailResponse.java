package com.yageum.fintech.domain.post.controller.response;

import com.yageum.fintech.domain.post.domain.Post;
import com.yageum.fintech.domain.post.infrastructure.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailResponse {

    @Schema(description = "게시글 id")
    private Long id;

    @Schema(description = "진행 현황 (완료 : true, 진행 중 : false")
    private boolean status;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용")
    private String content;

    @Schema(description = "작성자")
    private String userName;

    @Schema(description = "시작일 (2023.09.16 11:11)")
    private String startDate;

    @Schema(description = "댓글 갯수")
    private long commentSum;

    @Schema(description = "카테고리")
    Category category;

    public static PostDetailResponse from(Post post, String userName){

        return PostDetailResponse.builder()
                .id(post.getId())
//                .status()
                .title(post.getTitle())
                .content(post.getContent())
                .userName(userName)
                .startDate(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss")))
                .commentSum(post.getCommentList().size())
                .category(post.getCategory())
                .build();
    }
}
