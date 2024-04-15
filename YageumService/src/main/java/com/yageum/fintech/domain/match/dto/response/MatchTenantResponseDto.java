package com.yageum.fintech.domain.match.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yageum.fintech.domain.match.infrastructure.Matching;
import com.yageum.fintech.domain.match.infrastructure.MatchState;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchTenantResponseDto {

    private Long matchId;

    private Long tenantId;

    private MatchState state;

    public static MatchTenantResponseDto from(Tenant tenant, Matching matching){
        return MatchTenantResponseDto.builder()
                .matchId(matching.getMatchId())
                .tenantId(tenant.getTenantId())
                .state(matching.getState())
                .build();
    }
}
