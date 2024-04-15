package com.yageum.fintech.domain.match.service;

import com.yageum.fintech.domain.match.dto.request.UpdateMatchStateDto;
import com.yageum.fintech.domain.match.dto.response.MatchHouseResponseDto;
import com.yageum.fintech.domain.match.dto.response.MatchTenantResponseDto;

import java.util.List;

public interface MatchService {

    void createMatch(Long tenantId, Long houseId);

    List<MatchHouseResponseDto> getMatchesByTenantId(Long tenantId);

    List<MatchTenantResponseDto> getTenantsByHouseId(Long houseId);

    void updateMatchState(Long matchId, UpdateMatchStateDto updateDto);

    void deleteMatch(Long matchId);
}
