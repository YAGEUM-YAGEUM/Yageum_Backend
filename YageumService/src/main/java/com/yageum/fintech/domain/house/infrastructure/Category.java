package com.yageum.fintech.domain.house.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yageum.fintech.domain.tenant.infrastructure.Gender;

public enum Category {
    YEAR, //전세
    MONTH; //월세

    @JsonCreator
    public static Category from(String value) {
        for (Category category : Category.values()) {
            if (category.getValue().equalsIgnoreCase(value)) { // 대소문자 구분 없이 비교
                return category;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }

}
