package com.yageum.fintech.domain.post.service;

import java.util.Optional;

import com.yageum.fintech.domain.comment.service.port.CommentRepository;
import com.yageum.fintech.domain.post.controller.response.PostDetailResponse;
import com.yageum.fintech.domain.post.domain.Post;
import com.yageum.fintech.domain.post.infrastructure.PostRepository;
import com.yageum.fintech.domain.post.model.dto.request.CreatePostRequestServiceDto;
import com.yageum.fintech.domain.post.model.dto.request.DeletePostRequestServiceDto;
import com.yageum.fintech.domain.post.model.dto.request.RetrieveDetailPostRequestServiceDto;
import com.yageum.fintech.domain.post.model.dto.request.UpdatePostRequestServiceDto;
import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.domain.project.infrastructure.ProjectRepository;
import com.yageum.fintech.domain.tenant.service.UserServiceImpl2;
import com.yageum.fintech.global.config.jwt.jwtInterceptor.JwtContextHolder;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.UnauthorizedAccessException;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl {

    private final PostRepository postRepository;
    private final ProjectRepository projectRepository;
    private final UserServiceImpl2 userService;

    private final ResponseService responseService;

	public CommonResult createPost(CreatePostRequestServiceDto dto) {
        Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
        if (optionalProject.isEmpty()){
            return responseService.getFailResult(ExceptionList.INVALID_PROJECT_ID.getCode(), ExceptionList.INVALID_PROJECT_ID.getMessage());
        }
        Project project = optionalProject.get();

        // 요청자가 해당 프로젝트의 팀원인지 확인
        Long userId = JwtContextHolder.getUserId();
        checkMemberAuthorization(project, userId);

        Post post = dto.toEntity(userId);
        project.createPost(post);
        postRepository.save(post);
        return responseService.getSuccessfulResultWithMessage("기획/제작/편집 게시글 작성에 성공하였습니다.");
    }

    @Transactional(readOnly = true)
    public CommonResult retrieveDetailPost(RetrieveDetailPostRequestServiceDto dto) {
        Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
        if (optionalProject.isEmpty()){
            return responseService.getFailResult(ExceptionList.INVALID_PROJECT_ID.getCode(), ExceptionList.INVALID_PROJECT_ID.getMessage());
        }
        Project project = optionalProject.get();

        // 요청자가 해당 프로젝트의 팀원인지 확인
        checkMemberAuthorization(project, JwtContextHolder.getUserId());

        Optional<Post> optionalPost = postRepository.findById(dto.postId());
        if(optionalPost.isEmpty()){
            return responseService.getFailResult(ExceptionList.INVALID_POST_ID.getCode(), ExceptionList.INVALID_POST_ID.getMessage());
        }

        Post post = optionalPost.get();
        return responseService.getSingleResult(PostDetailResponse.from(post, userService.getUsername(post.getWriterId())));
    }

    public CommonResult updatePost(UpdatePostRequestServiceDto dto) {
        Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
        if (optionalProject.isEmpty()){
            return responseService.getFailResult(ExceptionList.INVALID_PROJECT_ID.getCode(), ExceptionList.INVALID_PROJECT_ID.getMessage());
        }
        Project project = optionalProject.get();

        // 요청자가 해당 프로젝트의 팀원인지 확인
        Long userId = JwtContextHolder.getUserId();
        checkMemberAuthorization(project, userId);

        Optional<Post> optionalPost = postRepository.findById(dto.postId());
        if(optionalPost.isEmpty()){
            return responseService.getFailResult(ExceptionList.INVALID_POST_ID.getCode(), ExceptionList.INVALID_POST_ID.getMessage());
        }

        Post post = optionalPost.get();
        if(post.getWriterId() != userId){
             return responseService.getFailResult(ExceptionList.NO_PERMISSION_TO_EDIT_POST.getCode(), ExceptionList.NO_PERMISSION_TO_EDIT_POST.getMessage());
        }

        post.update(dto);
        return responseService.getSuccessfulResultWithMessage("기획/제작/편집 게시글 수정에 성공하였습니다.");
    }

    public CommonResult deletePost(DeletePostRequestServiceDto dto) {
        Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
        if (optionalProject.isEmpty()){
            return responseService.getFailResult(ExceptionList.INVALID_PROJECT_ID.getCode(), ExceptionList.INVALID_PROJECT_ID.getMessage());
        }
        Project project = optionalProject.get();

        // 요청자가 해당 프로젝트의 팀원인지 확인
        Long userId = JwtContextHolder.getUserId();
        checkMemberAuthorization(project, userId);

        Optional<Post> optionalPost = postRepository.findById(dto.postId());
        if(optionalPost.isEmpty()){
            return responseService.getFailResult(ExceptionList.INVALID_POST_ID.getCode(), ExceptionList.INVALID_POST_ID.getMessage());
        }

        Post post = optionalPost.get();
        if(post.getWriterId() != userId){
            return responseService.getFailResult(ExceptionList.NO_PERMISSION_TO_EDIT_POST.getCode(), ExceptionList.NO_PERMISSION_TO_EDIT_POST.getMessage());
        }

        // 댓글들 삭제
        commentRepository.deleteAll(post);


        postRepository.delete(post);
        return responseService.getSuccessfulResultWithMessage("기획/제작/편집 게시글 삭제에 성공하였습니다.");
    }

    private static CommentRepository commentRepository;

    private void checkMemberAuthorization(Project project, Long userId){
        if(!project.isLeaderOrMember(userId))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
    }



}
