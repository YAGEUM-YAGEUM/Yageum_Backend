package com.yageum.fintech.domain.lessor.controller;

import com.yageum.fintech.domain.lessor.dto.request.LessorProfileDto;
import com.yageum.fintech.domain.lessor.dto.request.LessorRequestDto;
import com.yageum.fintech.domain.lessor.dto.response.GetLessorProfileDto;
import com.yageum.fintech.domain.lessor.service.LessorService;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantProfileDto;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "임대인 API", description = "임대인 회원가입, 프로필 등록 및 수정, 조회와 관련된 API")
@RestController
@RequestMapping("/api/v1/lessor")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LessorController {
    private final LessorService lessorService;
    private final ResponseService responseService;

    // 회원가입
    @Operation(summary = "임대인 회원가입")
    @PostMapping("/")
    public CommonResult register(@RequestBody LessorRequestDto lessorRequestDto){
        return lessorService.register(lessorRequestDto);
    }

    // 임차인 정보 - 프로필 등록
    @Operation(summary = "임대인 프로필 등록", description = "특정 임대인(사용자)의 프로필 정보를 등록하는 API")
    @PostMapping("/profile/{lessorId}")
    public CommonResult createLessorProfile(@PathVariable Long lessorId, @RequestBody LessorProfileDto lessorProfileDto){
        lessorService.createLessorProfile(lessorId, lessorProfileDto);
        return responseService.getSuccessfulResult();
    }

    // 임차인 정보 - 프로필 조회
    @Operation(summary = "임대인 프로필 조회", description = "특정 임차인(사용자)의 프로필 정보를 조회하는 API")
    @GetMapping("/profile/{lessorId}")
    public CommonResult getLessorProfile(@PathVariable Long lessorId){
        GetLessorProfileDto profile = lessorService.getLessorProfile(lessorId);
        return responseService.getSingleResult(profile);
    }

    // 임차인 정보 - 프로필 수정
    @Operation(summary = "임대인 프로필 수정", description = "프로필 ID로 프로필 정보를 수정하는 API")
    @PutMapping("/profile/{profileId}")
    public CommonResult updateLessorProfile(@PathVariable Long profileId, @RequestBody LessorProfileDto lessorProfileDto){
        lessorService.updateLessorProfile(profileId, lessorProfileDto);
        return responseService.getSuccessfulResult();
    }

}
