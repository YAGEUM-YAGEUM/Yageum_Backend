package com.yageum.fintech.domain.house.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Direction {
    EAST,
    WEST,
    SOUTH,
    NORTH;

    @JsonCreator
    public static Direction from(String value) {
        for (Direction direction : Direction.values()) {
            if (direction.getValue().equalsIgnoreCase(value)) { // 대소문자 구분 없이 비교
                return direction;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }

}
