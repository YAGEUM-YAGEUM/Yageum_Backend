package com.yageum.fintech.domain.project.infrastructure;

import com.yageum.fintech.domain.post.domain.Post;
import com.yageum.fintech.domain.project.dto.reqeust.CreateProjectRequestDto;
import com.yageum.fintech.domain.schedule.infrastructure.Schedule;
import com.yageum.fintech.domain.todo.infrastructure.ToDoEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "project")
public class Project {

    @Builder
    public Project(String name, LocalDate startDate, LocalDate finishDate, String description){
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.description = description;
        this.isChecked = false;
    }

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(name = "project_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @Column(name = "isChecked")
    private boolean isChecked;

    // Post(기획, 제작, 편집 게시글)와 연관 관계
    @OneToMany(mappedBy = "project", cascade = ALL, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = ALL, orphanRemoval = true)
    private List<Schedule> scheduleList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = ALL, orphanRemoval = true)
    private List<ToDoEntity> todoList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = ALL, orphanRemoval = true)
    private List<ProjectUser> projectUserList= new ArrayList<>();

    public void createPost(Post post){
        this.postList.add(post);
        post.setProject(this);
    }

    public void update(CreateProjectRequestDto projectUpdateRequestDto){
        this.name = projectUpdateRequestDto.getName();
        this.description = projectUpdateRequestDto.getDescription();
        this.startDate = projectUpdateRequestDto.startDateAsLocalDateType();
        this.finishDate = projectUpdateRequestDto.finishDateAsLocalDateType();
    }

    public void finish() {
        this.isChecked = true;
    }

    public Long getLeaderId(){
        for (ProjectUser projectUser : projectUserList)
            if(projectUser.getRole() == Role.LEADER)
                return projectUser.getUserId();
        return null;
    }

    public Set<Long> getMemberIdList(){
        Set<Long> memberIdList = new HashSet<>();
        for (ProjectUser projectUser : projectUserList)
            if(projectUser.getRole() == Role.MEMBER)
                memberIdList.add(projectUser.getUserId());
        return memberIdList;
    }

    public boolean isLeader(Long userId) {
        for(ProjectUser projectUser : projectUserList)
            if(projectUser.getUserId()==userId && projectUser.getRole()==Role.LEADER)
                return true;
        return  false;
    }

    public boolean isMember(Long userId) {
        for(ProjectUser projectUser : projectUserList)
            if(projectUser.getUserId()==userId && projectUser.getRole()==Role.MEMBER)
                return true;
        return  false;
    }

    public boolean isLeaderOrMember(Long userId){
        for(ProjectUser projectUser : projectUserList)
            if(projectUser.getUserId() == userId) return true;
        return  false;
    }
}
