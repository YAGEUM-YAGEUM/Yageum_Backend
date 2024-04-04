package com.yageum.fintech.domain.tenant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateTenantRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 5, message = "아이디는 5자 이상이어야 합니다.")
    @Schema(description = "아이디", example = "yageum123")
    private String username;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Schema(description = "비밀번호", example = "password123")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, message = "이름은 2자 이상이어야 합니다.")
    @Schema(description = "이름", example = "김청년")
    private String name;

    @NotNull(message = "전화번호를 입력해주세요.")
    @Size(min = 10, max = 15, message = "전화번호는 10자에서 15자 사이여야 합니다.")
    @Schema(description = "전화번호", example = "01012341234")
    private String phone;

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    @Size(min = 5, message = "이메일은 5자 이상이어야 합니다.")
    @Schema(description = "이메일", example = "yageumyageum00@gmail.com")
    private String email;

}
