package com.yageum.fintech.domain.post.service;

import com.yageum.fintech.domain.post.controller.port.PostReadService;
import com.yageum.fintech.domain.post.controller.response.PostResponse;
import com.yageum.fintech.domain.post.domain.Post;
import com.yageum.fintech.domain.post.infrastructure.Category;
import com.yageum.fintech.domain.post.infrastructure.PostRepository;
import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.domain.project.infrastructure.ProjectRepository;
import com.yageum.fintech.domain.tenant.service.UserServiceImpl2;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NonExistentException;
import com.yageum.fintech.global.model.Exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostReadServiceImpl implements PostReadService {

    private final PostRepository postRepository;
    private final ProjectRepository projectRepository;
    private final UserServiceImpl2 userService;

    @Override
    public List<PostResponse> readAll(Long projectId, Long userId, String category) {
        /**유저 추가해야 함*/
        Category getCategory = getCategory(category);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));

        // 요청자가 해당 프로젝트의 팀원인지 확인
        checkMemberAuthorization(project, userId);

        List<Post> postList = postRepository.findByCategoryAndProject(getCategory, project);
        List<PostResponse> postResponseList = new ArrayList<>();
        postList.forEach(post->{
            postResponseList.add(PostResponse.from(post, userService.getUsername(post.getWriterId())));
        });
        return postResponseList;
    }

    @Override
    public List<PostResponse> readThree(Long projectId, Long userId, String category) {
        /**유저 추가해야 함*/
        Category getCategory = getCategory(category);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));

        // 요청자가 해당 프로젝트의 팀원인지 확인
        checkMemberAuthorization(project, userId);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt"); // "createdAt" 필드를 기준으로 내림차순 정렬
        Pageable pageable = PageRequest.of(0, 3, sort); // 페이지 번호 0부터 3개의 결과를 가져옴
        List<Post> postList =  postRepository.findByCategoryAndProject(getCategory,project, pageable);
        List<PostResponse> postResponseList = new ArrayList<>();
        postList.forEach(post->{
            postResponseList.add(PostResponse.from(post, userService.getUsername(post.getWriterId())));
        });
        return postResponseList;
    }

    private Category getCategory(String category) {
        Category getCategory = null;
        switch (category) {
            case "PLANNING":
                getCategory = Category.PLANNING;
                break;
            case "PRODUCTION":
                getCategory = Category.PRODUCTION;
                break;
            case "EDITING":
                getCategory = Category.EDITING;
                break;
            default:
                new NonExistentException(ExceptionList.NON_EXISTENT_CATEGORY);
        }
        return getCategory;
    }

    private void checkMemberAuthorization(Project project, Long userId){
        if(!project.isLeaderOrMember(userId))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
    }

}
