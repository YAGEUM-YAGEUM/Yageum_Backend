package com.yageum.fintech.domain.user.dto.response;

import lombok.Data;

@Data
public class ResponseUser {
    private String email;
    private String name;
    private String phoneNumber;
    private boolean isApproved;
}
