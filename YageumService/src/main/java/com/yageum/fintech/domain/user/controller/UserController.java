package com.yageum.fintech.domain.user.controller;

import com.yageum.fintech.domain.user.dto.request.LoginRequest;
import com.yageum.fintech.domain.user.dto.request.CreateUserRequestDto;
import com.yageum.fintech.domain.user.dto.response.GetUserResponseDto;
import com.yageum.fintech.global.model.Exception.EmailVerificationResult;
import com.yageum.fintech.domain.user.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.user.service.UserService;
import com.yageum.fintech.global.config.jwt.JwtTokenProvider;
import com.yageum.fintech.global.model.Exception.BusinessLogicException;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 API", description = "로그인, 회원가입, 정보 조회/수정, 이메일 인증과 관련된 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    //health-check
    @Operation(summary = "사용자 서비스 상태 확인")
    @GetMapping("/health_check")
    public String status(){
        return "It's Working in User Service";
    }

    //로그인
    @Operation(summary = "사용자 로그인")
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginRequest loginRequest){
        JWTAuthResponse token = userService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    // 회원가입
    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateUserRequestDto createUserRequestDto){
        String response = userService.register(createUserRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 토큰 재발급
    @Operation(summary = "토큰 재발급")
    @PatchMapping("/reissue")
    public ResponseEntity<JWTAuthResponse> reissue(HttpServletRequest request,
                                                   HttpServletResponse response) {

        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        JWTAuthResponse newAccessToken = userService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }

    // 이메일 인증번호 전송
    @Operation(summary = "이메일 인증번호 전송")
    @PostMapping("/emails/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("email") @Valid String email) {
        userService.sendCodeToEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //이메일 인증번호 검증
    @Operation(summary = "이메일 인증번호 검증")
    @GetMapping("/emails/verifications")
    public ResponseEntity verificationEmail(@RequestParam("email") @Valid @Email String email,
                                            @RequestParam("code") String authCode) {
        EmailVerificationResult response = userService.verifiedCode(email, authCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 사용자 정보 조회 by userId
    @Operation(summary = "userId로 사용자 정보 조회")
    @GetMapping("/response_user/{userId}")
    public ResponseEntity<GetUserResponseDto> findUserResponseByUserId(@PathVariable("userId") Long userId) {
        try {
            GetUserResponseDto userResponse = userService.getUserResponseByUserId(userId);
            return ResponseEntity.ok().body(userResponse);
        } catch (BusinessLogicException e) {
            ExceptionList exceptionList = e.getExceptionList();

            return ResponseEntity.status(exceptionList.getCode()).
                    body(new GetUserResponseDto(exceptionList.getMessage()));
        }
    }

    // 사용자 정보 조회 by email
    @Operation(summary = "이메일로 사용자 정보 조회")
    @GetMapping("/response_userByEmail/{email}")
    public ResponseEntity<GetUserResponseDto> findUserResponseByEmail(@PathVariable String email) {
        try {
            GetUserResponseDto userResponse = userService.findUserResponseByEmail(email);
            return ResponseEntity.ok().body(userResponse);
        } catch (BusinessLogicException e) {
            ExceptionList exceptionList = e.getExceptionList();

            return ResponseEntity.status(exceptionList.getCode()).
                    body(new GetUserResponseDto(exceptionList.getMessage()));
        }
    }

}

