package com.yageum.fintech.domain.match.dto.request;

import com.yageum.fintech.domain.match.infrastructure.MatchState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMatchStateDto {
    private MatchState state;
}
