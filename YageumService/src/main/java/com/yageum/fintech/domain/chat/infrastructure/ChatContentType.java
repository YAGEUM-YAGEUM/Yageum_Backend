package com.yageum.fintech.domain.chat.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ChatContentType {
    ENTER,
    TALK,
    EXIT;

    @JsonCreator
    public static ChatContentType from(String value) {
        for (ChatContentType contentType : ChatContentType.values()) {
            if (contentType.getValue().equalsIgnoreCase(value)) { // 대소문자 구분 없이 비교
                return contentType;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }
}
