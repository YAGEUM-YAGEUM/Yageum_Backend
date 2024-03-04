package com.yageum.fintech.domain.todo.controller;

import com.yageum.fintech.domain.todo.dto.ToDoRequestDto;
import com.yageum.fintech.domain.todo.infrastructure.ToDoEntity;
import com.yageum.fintech.domain.todo.service.ToDoService;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "체크리스트 CRUD API", description = "체크리스트(할일) 관련 API")
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ToDoController {
    @Autowired
    ToDoService toDoService;
    @Autowired
    ResponseService responseService;

    @GetMapping("/todo/ping")
    @Operation(summary = "Ping 테스트", description = "서버 피드백 확인용")
    public String ping() {
        return "fong";
    }

    // 체크박스 등록
    @PostMapping("/projects/{projectId}/todo")
    @Operation(summary = "할일 등록", description = "할일 등록 api")
    public CommonResult registerToDo(@PathVariable Long projectId, @RequestBody ToDoRequestDto toDoRequestDto){
        toDoService.registerToDo(projectId, toDoRequestDto);
        return responseService.getSuccessfulResult();
    }

    //체크박스 전체 조회
    @GetMapping("/projects/{projectId}/todo")
    @Operation(summary = "할일 조회", description = "할일 조회 api")
    public CommonResult showToDo(@PathVariable Long projectId) {
        List<ToDoEntity> toDoEntity = toDoService.getToDo(projectId);
        CommonResult commonResult = responseService.getListResult(toDoEntity);
        return commonResult;
    }
    //체크박스 하나만 선택
    @GetMapping("/todo/{todoIndex}")
    @Operation(summary = "할일 선택 조회", description = "할일 선택 조회 api")
    public CommonResult showToDoOne(@PathVariable Long todoIndex) {
        ToDoEntity toDoEntity = toDoService.showToDoOne(todoIndex);
        CommonResult commonResult = responseService.getSingleResult(toDoEntity);
        return commonResult;
    }
    //체크박스 수정
    @PutMapping("/todo/{todoIndex}")
    @Operation(summary = "할일 업데이트", description = "할일 업데이트 api")
    public CommonResult updateToDo(@PathVariable Long todoIndex, @RequestBody ToDoRequestDto toDoRequestDto) {
        toDoService.updateToDo(todoIndex, toDoRequestDto);
        return responseService.getSuccessfulResult();
    }
    //체크박스 삭제
    @DeleteMapping("/todo/{todoIndex}")
    @Operation(summary = "할일 삭제", description = "할일 삭제 api")
    public CommonResult deleteToDo(@PathVariable Long todoIndex) {
        toDoService.deleteToDo(todoIndex);
        return responseService.getSuccessfulResult();
    }

    //체크박스 클릭(완료 표시)
    @GetMapping("/todo/finish/{todoIndex}")
    @Operation(summary = "할일 완료 표시", description = "할일 완료 표시 api")
    public CommonResult finishToDo(@PathVariable Long todoIndex) {
        toDoService.finishToDo(todoIndex);
        return responseService.getSuccessfulResult();
    }

}
