package com.yageum.fintech.domain.match.controller;

import com.yageum.fintech.domain.house.dto.response.HouseResponseDto;
import com.yageum.fintech.domain.match.dto.request.UpdateMatchStateDto;
import com.yageum.fintech.domain.match.dto.response.MatchHouseResponseDto;
import com.yageum.fintech.domain.match.dto.response.MatchTenantResponseDto;
import com.yageum.fintech.domain.match.service.MatchService;
import com.yageum.fintech.domain.tenant.dto.request.TenantProfileDto;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관심 매물 API", description = "관심매물 등록, 상태 수정 및 삭제, 관심 리스트, 임차인 조회와 관련된 API")
@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MatchController {

    private final MatchService matchService;
    private final ResponseService responseService;

    // 관심 매물 등록
    @Operation(summary = "관심 매물 등록", description = "특정 임차인이 관심 있는 매물을 등록하는 API")
    @PostMapping("/{tenantId}/{houseId}")
    public CommonResult createMatch(@PathVariable Long tenantId, @PathVariable Long houseId) {
        matchService.createMatch(tenantId, houseId);
        return responseService.getSuccessfulResult();
    }

    // 임차인 관심 매물 리스트 조회
    @Operation(summary = "관심 매물 리스트 조회", description = "특정 임차인(사용자)의 관심 매물 리스트를 조회하는 API")
    @GetMapping("/house_list/{tenantId}")
    public CommonResult getHouseList(@PathVariable Long tenantId){
        List<MatchHouseResponseDto> houseList = matchService.getMatchesByTenantId(tenantId);
        return responseService.getListResult(houseList);
    }

    // 특정 매물에 대한 임차인 리스트 조회
    @Operation(summary = "임차인 리스트 조회", description = "특정 매물에 대해 관심이 있는 임차인 리스트를 조회하는 API")
    @GetMapping("/tenant_list/{houseId}")
    public CommonResult getTenantList(@PathVariable Long houseId){
        List<MatchTenantResponseDto> tenantList = matchService.getTenantsByHouseId(houseId);
        return responseService.getListResult(tenantList);
    }

    // 관심 매물 상태 수정
    @Operation(summary = "관심 매물 상태 수정", description = "특정 관심 매물의 상태를 수정하는 API")
    @PutMapping("/{matchId}/state")
    public CommonResult updateMatchState(@PathVariable Long matchId, @RequestBody UpdateMatchStateDto updateMatchStateDto) {
        matchService.updateMatchState(matchId, updateMatchStateDto);
        return responseService.getSuccessfulResult();
    }

    // 관심 매물 삭제
    @Operation(summary = "관심 매물 삭제", description = "특정 관심 매물을 삭제하는 API")
    @DeleteMapping("/{matchId}")
    public CommonResult deleteMatch(@PathVariable Long matchId) {
        matchService.deleteMatch(matchId);
        return responseService.getSuccessfulResult();
    }

}