package com.yageum.fintech.domain.house.controller;

import com.yageum.fintech.domain.house.dto.request.HouseRequestDto;
import com.yageum.fintech.domain.house.dto.response.HouseResponseDto;
import com.yageum.fintech.domain.house.service.HouseService;
import com.yageum.fintech.domain.lessor.dto.request.LessorProfileDto;
import com.yageum.fintech.domain.lessor.dto.response.GetLessorProfileDto;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "매물 API", description = "매물 CRUD API")
@RestController
@RequestMapping("/api/v1/lessor")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HouseController {
    private final HouseService houseService;
    private final ResponseService responseService;

    // 임대인 정보 - 매물 등록
    @Operation(summary = "매물 등록", description = "특정 임대인(사용자)의 매물을 등록하는 API")
    @PostMapping("/property/{lessorId}")
    public CommonResult createHouse(@PathVariable Long lessorId, @RequestBody HouseRequestDto houseRequestDto){
        houseService.createHouse(lessorId, houseRequestDto);
        return responseService.getSuccessfulResult();
    }

    // 임대인 정보 - 매물 조회
    @Operation(summary = "매물 리스트 조회", description = "특정 임차인(사용자)의 매물 리스트 조회하는 API")
    @GetMapping("/property/{lessorId}")
    public CommonResult getHouseList(@PathVariable Long lessorId){
        List<HouseResponseDto> houses = houseService.getHousesByLessorId(lessorId);
        return responseService.getListResult(houses);
    }

    // 임대인 정보 - 매물 수정
    @Operation(summary = "매물 수정", description = "매물 ID로 매등 정보를 수정하는 API")
    @PutMapping("/property/{lessorId}")
    public CommonResult updateHouse(@PathVariable Long houseId, @RequestBody HouseRequestDto houseRequestDto){
        houseService.updateHouse(houseId, houseRequestDto);
        return responseService.getSuccessfulResult();
    }

    //임대인 정보-매물 삭제
}
