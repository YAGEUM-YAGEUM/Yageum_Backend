package com.yageum.fintech.domain.lessor.service;

import com.yageum.fintech.domain.lessor.dto.request.LessorProfileDto;
import com.yageum.fintech.domain.lessor.dto.request.LessorRequestDto;
import com.yageum.fintech.domain.lessor.dto.response.GetLessorProfileDto;
import com.yageum.fintech.global.model.Result.CommonResult;

public interface LessorService {

    CommonResult register(LessorRequestDto lessorRequestDto);

    void createLessorProfile(Long lessorId, LessorProfileDto lessorProfileDto);

    void updateLessorProfile(Long profileId, LessorProfileDto lessorProfileDto);

    GetLessorProfileDto getLessorProfile(Long lessorId);

}

