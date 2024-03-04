package com.yageum.fintech.domain.comment.controller;

import com.yageum.fintech.domain.comment.controller.port.CommentService;
import com.yageum.fintech.domain.comment.controller.response.CommentResponse;
import com.yageum.fintech.domain.comment.domain.Comment;
import com.yageum.fintech.domain.comment.domain.CommentCreate;
import com.yageum.fintech.domain.comment.domain.CommentUpdate;
import com.yageum.fintech.domain.user.service.UserServiceImpl;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "[기획 /제작/ 편집] 댓글 작성, 수정, 삭제, 조회 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;
    private final ResponseService responseService;

    private final UserServiceImpl userService;

    private String getName(long writtenId){
        return userService.getUsername(writtenId);
    }

    @Operation(summary = "댓글 작성")
    @PostMapping("/posts/{postId}/comment")
    public CommonResult create(@PathVariable Long postId,
                               @Valid @RequestBody CommentCreate commentCreate){
        Comment comment = commentService.create(postId, commentCreate);
        return responseService.getSingleResult(CommentResponse.from(comment, getName(comment.getWriteId())));
    }

    @Operation(summary = "게시글 Id에 따른 댓글 읽기")
    @GetMapping("/posts/{postId}/comments")
    public CommonResult read(@PathVariable Long postId){
        List<Comment> comments = commentService.read(postId);

        List<CommentResponse> commentResponses = new ArrayList<>();
        comments.forEach(comment -> {
            commentResponses.add(CommentResponse.from(comment, getName(comment.getWriteId())));
        });
        return responseService.getSingleResult(commentResponses);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/comment/{commentId}")
    public CommonResult update(@PathVariable Long commentId,
                               @RequestBody CommentUpdate commentUpdate){
        Comment comment = commentService.update(commentId, commentUpdate);
        return responseService.getSingleResult(CommentResponse.from(comment, getName(comment.getWriteId())));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comment/{commentId}")
    public CommonResult delete(@PathVariable Long commentId){
        commentService.delete(commentId);
        return responseService.getSuccessfulResult();
    }

}
