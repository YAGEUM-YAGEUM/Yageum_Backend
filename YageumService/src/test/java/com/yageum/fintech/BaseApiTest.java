package com.yageum.fintech;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.mju.management.domain.project.infrastructure.*;
import com.yageum.fintech.domain.project.infrastructure.*;
import com.yageum.fintech.global.config.jwtInterceptor.JwtContextHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseApiTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    private static WireMockServer wireMockServer;
    private static final Integer port = 8081;
    protected static final Long leaderId = 1L;
    protected static final Long memberId = 2L;
    protected static final Long outsiderId = 3L;

    public enum UserServiceState{
        NORMAL, NON_EXISTENT_USER, INTERNAL_SERVER_ERROR, TIME_OUT_ERROR
    }


    @BeforeAll
    public static void startWireMockServer() {
        wireMockServer = new WireMockServer(port);
        wireMockServer.start();
    }

    @AfterAll
    public static void stopWireMockServer() {
        wireMockServer.stop();
    }

    @AfterEach
    public void deleteAllProject() {
        projectRepository.deleteAll();
    }

    @AfterEach
    public void clearJwtContext() {
        JwtContextHolder.clear();
    }

    @AfterEach
    public void resetWireMockServer() {
        wireMockServer.resetAll();
    }

    protected void setUserServiceState(UserServiceState userServiceState, Long userId) {
        if(userServiceState == UserServiceState.NORMAL)
            wireMockServer.stubFor(get(urlEqualTo("/user-service/response_userById/" + userId))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{ " +
                                    "\"id\": " + userId + ", " +
                                    "\"email\": " + "\"test" + userId + "\"" + ", " +
                                    "\"name\": " + "\"test" + userId + "\"" + ", " +
                                    "\"phoneNumber\": " + "\"test" + userId + "\"" + ", " +
                                    "\"isApproved\": true " +
                                    "}")));
        if(userServiceState == UserServiceState.NON_EXISTENT_USER)
            wireMockServer.stubFor(get(urlEqualTo("/user-service/response_userById/" + userId))
                    .willReturn(aResponse()
                            .withStatus(500)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{ \"error\": \"User not found\" }")));
        if(userServiceState == UserServiceState.INTERNAL_SERVER_ERROR)
            wireMockServer.stubFor(get(urlEqualTo("/user-service/response_userById/" + userId))
                    .willReturn(aResponse()
                            .withStatus(500)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{ \"error\": \"Internal Server Error\" }")));
        if(userServiceState == UserServiceState.TIME_OUT_ERROR)
            wireMockServer.stubFor(get(urlEqualTo("/user-service/response_userById/" + userId))
                    .willReturn(aResponse()
                            .withStatus(500)
                            .withFixedDelay(6000)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{ \"error\": \"Internal Server Error\" }")));
    }

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected ProjectUserRepository projectUserRepository;

    protected final String projectName = "소코아 프로젝트";
    protected final String projectDescription = "소코아 프로젝트입니다.";
    protected final String projectStartDate = "2023-09-01";
    protected final String projectFinishDate = "2023-12-15";

    protected Project createProject(Long leaderId){
        // create Project
        Project project = projectRepository.save(Project
                .builder()
                .name(projectName)
                .description(projectDescription)
                .startDate(LocalDate.parse(projectStartDate))
                .finishDate(LocalDate.parse(projectFinishDate))
                .build()
        );

        // create ProjectUser(leader)
        projectUserRepository.save(ProjectUser
                .builder()
                .userId(leaderId)
                .project(project)
                .role(Role.LEADER)
                .build()
        );
        return project;
    }

    protected ProjectUser createProjectUser(Long memberId, Project project){
        // create ProjectUser(member)
        return projectUserRepository.save(ProjectUser
                .builder()
                .userId(memberId)
                .project(project)
                .role(Role.MEMBER)
                .build()
        );
    }

}
