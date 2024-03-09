package com.yageum.fintech.domain.post;

import com.yageum.fintech.BaseApiTest;
import com.yageum.fintech.domain.post.domain.Post;
import com.yageum.fintech.domain.post.infrastructure.Category;
import com.yageum.fintech.domain.post.infrastructure.PostRepository;
import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.global.config.jwt.jwtInterceptor.JwtContextHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class PostApiTest extends BaseApiTest {
    @Autowired
    protected PostRepository postRepository;

    protected final String postTitle = "안녕하세요";
    protected final String postContent = "반갑습니다.";
    protected final Category postCategory = Category.EDITING;

    protected Post createPost(Project project, Long writerId) {
        Post post =  Post.builder()
                .title(postTitle)
                .content(postContent)
                .category(postCategory)
                .writerId(writerId)
                .build();
        post.setProject(project);
        return postRepository.save(post);
    }

//    @DisplayName("글쓰기 성공")
//    @Test
//    public void createPost_Success() throws Exception {
//        //given
//        JwtContextHolder.setUserId(leaderId);
//
//        Project project = createProject(leaderId);
//
//        CreatePostRequestDto createPostRequestDto = CreatePostRequestDto
//                .builder()
//                .projectId(project.getProjectId())
//                .title(postTitle)
//                .content(postContent)
//                .category(postCategory)
//                .build();
//
//        String url = "/api/posts";
//
//        String requestBody = objectMapper.writeValueAsString(createPostRequestDto);
//
//        //when
//        ResultActions result = mockMvc.perform(post(url)
//                .contentType(APPLICATION_JSON_VALUE)
//                .content(requestBody));
//
//        //then
//        result.andExpect(jsonPath("$.code").value(200));
//
//        List<Post> postList = postRepository.findAll();
//        assertThat(postList.size()).isEqualTo(1);
//        Post post = postList.get(0);
//        assertThat(post.getProject().getProjectId()).isEqualTo(project.getProjectId());
//        assertThat(post.getWriterId()).isEqualTo(leaderId);
//        assertThat(post.getTitle()).isEqualTo(postTitle);
//        assertThat(post.getContent()).isEqualTo(postContent);
//        assertThat(post.getCategory()).isEqualTo(postCategory);
//    }
//
//    @DisplayName("글쓰기 실패: 요청자가 프로젝트 소속이 아님")
//    @Test
//    public void createPost_Fail_UnauthorizedAccess() throws Exception {
//        //given
//        JwtContextHolder.setUserId(outsiderId);
//
//        Project project = createProject(leaderId);
//
//        CreatePostRequestDto createPostRequestDto = CreatePostRequestDto
//                .builder()
//                .projectId(project.getProjectId())
//                .title(postTitle)
//                .content(postContent)
//                .category(postCategory)
//                .build();
//
//        String url = "/api/posts";
//
//        String requestBody = objectMapper.writeValueAsString(createPostRequestDto);
//
//        //when
//        ResultActions result = mockMvc.perform(post(url)
//                .contentType(APPLICATION_JSON_VALUE)
//                .content(requestBody));
//
//        //then
//        result.andExpect(jsonPath("$.code").value(8000));
//    }
//
//    @DisplayName("글쓰기 실패: 프로젝트가 존재하지 않음")
//    @Test
//    public void createPost_Fail_NonExistentProject() throws Exception {
//        //given
//        JwtContextHolder.setUserId(leaderId);
//
//        CreatePostRequestDto createPostRequestDto = CreatePostRequestDto
//                .builder()
//                .projectId(1L)
//                .title(postTitle)
//                .content(postContent)
//                .category(postCategory)
//                .build();
//
//        String url = "/api/posts";
//
//        String requestBody = objectMapper.writeValueAsString(createPostRequestDto);
//
//        //when
//        ResultActions result = mockMvc.perform(post(url)
//                .contentType(APPLICATION_JSON_VALUE)
//                .content(requestBody));
//
//        //then
//        result.andExpect(jsonPath("$.code").value(5010));
//    }

    @DisplayName("게시글 상세 조회 성공: 유저 서비스의 상태가 정상인 경우")
    @Test
    public void getPost_Success_UserServiceState_Normal() throws Exception {

        //given (테스트 준비) : 게시글을 생성하고, 유저 서비스의 상태를 설정
        Long userId = 1L;
        Project project = createProject(userId); // 팀장 아이디가 userId인 프로젝트 생성
        Post post = createPost(project, userId); // 프로젝트 내에 작성자 아이디가 userId인 게시글 생성
        setUserServiceState(UserServiceState.NORMAL, userId); // 유저서비스에 아이디가 userId인 유저 상세 정보를 요청할 때, 유저서비스의 상태를 정상으로 설정
        JwtContextHolder.setUserId(userId); // api 요청자 아이디를 userId로 설정

        //when (테스트 실행) : 매니지먼트 서비스의 게시글 상세 조회 api 호출 및 응답 데이터 저장
        ResultActions result = mockMvc.perform(get("/api/posts")
                .param("projectId", String.valueOf(project.getProjectId()))
                .param("postId", String.valueOf(post.getId()))
                .accept(APPLICATION_JSON_VALUE)
        );

        //then (테스트 검증) : 응답 데이터 중 게시글 작성자 이름은 "test" + userId 이어야 한다.
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(post.getId()))
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.userName").value("test" + post.getWriterId()))
                .andExpect(jsonPath("$.data.category").value(post.getCategory().name()));

    }

    @DisplayName("게시글 상세 조회 성공: 유저 서비스에 internal sever error가 발생한 경우")
    @Test
    public void getPost_Success_UserServiceState_InternalServerError() throws Exception {

        //given (테스트 준비) : 게시글을 생성하고, 유저 서비스의 상태를 설정
        Long userId = 1L;
        Project project = createProject(userId); // 팀장 아이디가 userId인 프로젝트 생성
        Post post = createPost(project, userId); // 프로젝트 내에 작성자 아이디가 userId인 게시글 생성
        setUserServiceState(UserServiceState.INTERNAL_SERVER_ERROR, userId); // 유저 서비스에 아이디가 userId인 유저 상세 정보를 요청할 때, 유저서비스가 interanl server error를 발생시키도록 설정
        JwtContextHolder.setUserId(userId); // api 요청자 아이디를 userId로 설정

        //when (테스트 실행) : 매니지먼트 서비스의 게시글 상세 조회 api 호출 및 응답 데이터 저장
        ResultActions result = mockMvc.perform(get("/api/posts")
                .param("projectId", String.valueOf(project.getProjectId()))
                .param("postId", String.valueOf(post.getId()))
                .accept(APPLICATION_JSON_VALUE)
        );

        //then (테스트 검증) : 응답 데이터 중 게시글 작성자 이름은 "(내부 서버 오류)"이어야 한다.
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(post.getId()))
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.userName").value("(내부 서버 오류)"))
                .andExpect(jsonPath("$.data.category").value(post.getCategory().name()));

    }

    @DisplayName("게시글 상세 조회 성공: 유저 서비스의 응답이 5초 이상 지연되는 경우")
    @Test
    public void getPost_Success_UserServiceState_timeOutError() throws Exception {

        //given (테스트 준비) : 게시글을 생성하고, 유저 서비스의 상태를 설정
        Long userId = 1L;
        Project project = createProject(userId); // 팀장 아이디가 userId인 프로젝트 생성
        Post post = createPost(project, userId); // 프로젝트 내에 작성자 아이디가 userId인 게시글 생성
        setUserServiceState(UserServiceState.TIME_OUT_ERROR, userId); // 아이디가 userId인 유저 상세 정보를 요청할 때, 유저서비스의 응답이 5초 이상 지연되도록 설정
        JwtContextHolder.setUserId(userId); // api 요청자 아이디를 userId로 설정

        //when (테스트 실행) : 매니지먼트 서비스의 게시글 상세 조회 api 호출 및 응답 데이터 저장
        ResultActions result = mockMvc.perform(get("/api/posts")
                .param("projectId", String.valueOf(project.getProjectId()))
                .param("postId", String.valueOf(post.getId()))
                .accept(APPLICATION_JSON_VALUE)
        );

        //then (테스트 검증) : 응답 데이터 중 게시글 작성자 이름은 "(응답 시간 초과)"이어야 한다.
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(post.getId()))
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.userName").value("(응답 시간 초과)"))
                .andExpect(jsonPath("$.data.category").value(post.getCategory().name()));

    }
}
