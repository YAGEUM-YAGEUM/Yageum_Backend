package com.yageum.fintech.domain.match.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MatchState {
    PREREQUEST,
    VIEWING,
    CONTRACTING,
    COMPLETED;

    @JsonCreator
    public static MatchState from(String value) {
        for (MatchState matchState : MatchState.values()) {
            if (matchState.getValue().equalsIgnoreCase(value)) { // 대소문자 구분 없이 비교
                return matchState;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }

}
