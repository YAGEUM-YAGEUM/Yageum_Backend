package com.yageum.fintech.domain.lessor.dto.response;

import com.yageum.fintech.domain.lessor.infrastructure.Lessor;
import lombok.Getter;

@Getter
public class GetLessorResponseDto {

    private Long lessorId;
    private String username;
    private String name;
    private String phone;
    private String email;
    private String message;

    public static GetLessorResponseDto from(Lessor lessor){
        return new GetLessorResponseDto(
                lessor.getLessorId(),
                lessor.getUsername(),
                lessor.getEmail(),
                lessor.getName(),
                lessor.getPhone()
        );
    }

    //오류 응답
    public GetLessorResponseDto(String message) {
        this.message = message;
    }

    // 필드를 가지고 있는 생성자
    public GetLessorResponseDto(Long lessorId, String username, String email, String name, String phone) {
        this.lessorId = lessorId;
        this.username = username;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
}
