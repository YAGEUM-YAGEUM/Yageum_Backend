package com.yageum.fintech.domain.project.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    LEADER("팀장"),
    MEMBER("팀원");

    private final String description;
}
