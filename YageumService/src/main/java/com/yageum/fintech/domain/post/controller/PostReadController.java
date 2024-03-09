package com.yageum.fintech.domain.post.controller;

import com.yageum.fintech.domain.post.controller.port.PostReadService;
import com.yageum.fintech.domain.post.controller.response.PostResponse;
import com.yageum.fintech.global.config.jwt.jwtInterceptor.JwtContextHolder;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[기획/제작/편집] 게시글 조회 API")
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PostReadController {

    private final PostReadService postReadService;
    private final ResponseService responseService;

    // 전체 조희 + 페이징
    @Operation(summary = "기획/제작/편집 게시글 전체 조회 API (category : PLANNING, PRODUCTION, EDITING)")
    @GetMapping("/{projectId}/posts/all")
    public CommonResult readAll(@RequestParam("category") String category,
                                @PathVariable Long projectId){
        Long userId = JwtContextHolder.getUserId();
        List<PostResponse> responseList = postReadService.readAll(projectId, userId, category);
        return responseService.getListResult(responseList);
    }

    @Operation(summary = "기획/제작/편집 게시글 상위 3개 조회 API (category : PLANNING, PRODUCTION, EDITING)")
    @GetMapping("/{projectId}/posts/recent")
    public CommonResult readThree(@PathVariable Long projectId,
                                  @RequestParam("category") String category){
        Long userId = JwtContextHolder.getUserId();
        List<PostResponse> responseList = postReadService.readThree(projectId, userId, category);
        return responseService.getListResult(responseList);
    }

    // 검색 + 페이징

}
