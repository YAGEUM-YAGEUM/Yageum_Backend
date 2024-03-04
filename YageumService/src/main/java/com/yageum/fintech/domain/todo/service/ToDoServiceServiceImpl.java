package com.yageum.fintech.domain.todo.service;

import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.domain.project.infrastructure.ProjectRepository;
import com.yageum.fintech.domain.project.infrastructure.ProjectUserRepository;
import com.yageum.fintech.domain.todo.dto.ToDoRequestDto;
import com.yageum.fintech.domain.todo.infrastructure.ToDoEntity;
import com.yageum.fintech.domain.todo.infrastructure.ToDoJpaRepository;
import com.yageum.fintech.global.config.jwtInterceptor.JwtContextHolder;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NonExistentException;
import com.yageum.fintech.global.model.Exception.UnauthorizedAccessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoServiceServiceImpl implements ToDoService {
    private final ToDoJpaRepository toDoJpaRepository;
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;

    @Override
    @Transactional
    public void registerToDo(Long projectId, ToDoRequestDto toDoRequestDto) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            if(!project.isLeaderOrMember(JwtContextHolder.getUserId()))
                throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
            ToDoEntity toDoEntity = ToDoEntity.builder()
                    .todoContent(toDoRequestDto.getTodoContent())
                    .todoEmergency(toDoRequestDto.isTodoEmergency())
                    .project(project)
                    .build();
            toDoJpaRepository.save(toDoEntity);
        } else {
            throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);
        }
    }

    @Override
    @Transactional
    public List<ToDoEntity> getToDo(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            projectUserRepository.findByProjectAndUserId(project, JwtContextHolder.getUserId())
                    .orElseThrow(() -> new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS));
            List<ToDoEntity> toDoEntity = toDoJpaRepository.findByProjectAndOrderByConditions(project);
            if (!toDoEntity.isEmpty()) {
                return toDoEntity;
            }else {
                throw new NonExistentException(ExceptionList.NON_EXISTENT_CHECKLIST);
            }
        }else {
            throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);
        }
    }

    @Override
    @Transactional
    public void deleteToDo(Long todoIndex) {
        Optional<ToDoEntity> optionalToDoEntity = toDoJpaRepository.findById(todoIndex);
        if (optionalToDoEntity.isPresent()) { // 해당 index의 CheckList가 존재하는 경우
            ToDoEntity toDoEntity = optionalToDoEntity.get();
            projectUserRepository.findByProjectAndUserId(toDoEntity.getProject(), JwtContextHolder.getUserId())
                    .orElseThrow(() -> new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS));
            toDoJpaRepository.deleteById(todoIndex);
        } else {
            throw new NonExistentException(ExceptionList.NON_EXISTENT_CHECKLIST);
        }
    }

    @Override
    @Transactional
    public ToDoEntity showToDoOne(Long todoIndex) {
        Optional<ToDoEntity> optionalToDo = toDoJpaRepository.findById(todoIndex);
        if (optionalToDo.isPresent()) {
            ToDoEntity toDoEntity = optionalToDo.get();
            projectUserRepository.findByProjectAndUserId(toDoEntity.getProject(), JwtContextHolder.getUserId())
                    .orElseThrow(() -> new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS));
            return toDoEntity;
        } else {
            throw new NonExistentException(ExceptionList.NON_EXISTENT_CHECKLIST);
        }
    }

    @Override
    @Transactional
    public void updateToDo(Long todoIndex, ToDoRequestDto toDoRequestDto) {
        Optional<ToDoEntity> optionalToDo = toDoJpaRepository.findById(todoIndex);
        if (optionalToDo.isPresent()) {
            ToDoEntity toDoEntity = optionalToDo.get();
            projectUserRepository.findByProjectAndUserId(toDoEntity.getProject(), JwtContextHolder.getUserId())
                    .orElseThrow(() -> new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS));
            toDoEntity.update(toDoRequestDto.getTodoContent());
            toDoJpaRepository.save(toDoEntity);
        } else {
            throw new NonExistentException(ExceptionList.NON_EXISTENT_CHECKLIST);
        }
    }

    @Override
    @Transactional
    public void finishToDo(Long todoIndex) {
        Optional<ToDoEntity> optionalToDo = toDoJpaRepository.findById(todoIndex);
        if (optionalToDo.isPresent()) {
            ToDoEntity toDoEntity = optionalToDo.get();
            projectUserRepository.findByProjectAndUserId(toDoEntity.getProject(), JwtContextHolder.getUserId())
                    .orElseThrow(() -> new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS));
            toDoEntity.finish(toDoEntity.isChecked());
            toDoJpaRepository.save(toDoEntity);
        } else {
            throw new NonExistentException(ExceptionList.NON_EXISTENT_CHECKLIST);
        }
    }
}
