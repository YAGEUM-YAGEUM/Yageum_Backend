package com.yageum.fintech.domain.tenant.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender{
    MALE,
    FEMALE;

    @JsonCreator
    public static Gender from(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue().equalsIgnoreCase(value)) { // 대소문자 구분 없이 비교
                return gender;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }
}
