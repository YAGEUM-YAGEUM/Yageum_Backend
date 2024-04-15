package com.yageum.fintech.domain.match.dto.response;

import com.yageum.fintech.domain.house.infrastructure.House;
import com.yageum.fintech.domain.match.infrastructure.Matching;
import com.yageum.fintech.domain.match.infrastructure.MatchState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchHouseResponseDto {

    private Long matchId;

    private Long houseId;

    private MatchState state;

    public static MatchHouseResponseDto from(House house, Matching matching){
        return MatchHouseResponseDto.builder()
                .matchId(matching.getMatchId())
                .houseId(house.getHouseId())
                .state(matching.getState())
                .build();
    }
}
