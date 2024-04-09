package com.yageum.fintech.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 5, message = "아이디는 5자 이상이어야 합니다.")
    @Schema(description = "아이디", example = "yageum123")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
    @Schema(description = "비밀번호", example = "password123")
    private String password;

}
