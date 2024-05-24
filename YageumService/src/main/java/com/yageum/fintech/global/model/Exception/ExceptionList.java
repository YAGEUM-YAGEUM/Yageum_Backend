package com.yageum.fintech.global.model.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionList {

    UNKNOWN(-9999, "알 수 없는 오류가 발생하였습니다."),
    INVALID_INPUT_VALUE(5009, "유효성 검사에 실패하였습니다."),

    BAD_REQUEST(400, "잘못된 요청입니다"),
    TOKEN_IS_NOT_SAME(401, "액세스 토큰과 리프레시 토큰이 일치하지 않습니다"),
    HEADER_REFRESH_TOKEN_NOT_EXISTS(400, "요청 헤더에 리프레시 토큰이 누락되었습니다"),
    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다"),
    MEMBER_EXISTS(409, "이미 존재하는 회원입니다"),
    ALREADY_EXISTS(410, "이미 존재하는 아이디입니다"),
    ALREADY_EXISTS_WALLET(411, "이미 지갑 계정이 존재합니다"),
    NO_SUCH_ALGORITHM(500, "존재하지 않는 알고리즘입니다"),
    UNABLE_TO_SEND_EMAIL(501, "이메일을 전송할 수 없습니다"),

    // Jwt
    NULL_JWT_TOKEN(7000, "토큰이 존재하지 않습니다."),
    EXPIRED_JWT(7001, "만료된 토큰입니다."),
    PREMATURE_JWT(7002, "아직 활성화되지 않은 토큰입니다."),
    UNSUPPORTED_JWT(7003, "지원되지 않는 토큰 형식 또는 구조입니다."),
    MALFORMED_JWT(7004, "토큰의 형식이 잘못되었습니다."),
    SIGNATURE_JWT(7005, "토큰의 서명이 검증되지 않았습니다."),
    ILLEGAL_ARGUMENT_JWT(7006, "잘못된 인자가 전달되었습니다."),
    SECURITY_JWT(7007, "보안 관련 문제로 토큰 검증이 실패했습니다."),
    INVALID_JWT(7008, "유효하지 않은 토큰입니다."),

    // TENANT
    NON_EXISTENT_TENANT(5010, "존재하지 않는 임차인입니다."),
    NON_EXISTENT_TENANT_PROFILE(5011, "임차인 프로필이 존재하지 않습니다"),
    TENANT_PROFILE_ALREADY_EXISTS(5012, "이미 해당 tenantId에 대한 프로필이 존재합니다."),
    NON_EXISTENT_TENANTLIST(5013, "임차인 목록이 존재하지 않습니다."),

    //LESSOR
    NON_EXISTENT_LESSOR(5020, "존재하지 않는 임대인입니다."),
    NON_EXISTENT_LESSOR_PROFILE(5021, "임대인 프로필이 존재하지 않습니다"),
    LESSOR_PROFILE_ALREADY_EXISTS(5022, "이미 해당 lessorId에 대한 프로필이 존재합니다."),
    NON_EXISTENT_LESSORLIST(5023, "임대인 목록이 존재하지 않습니다."),

    //HOUSE
    NON_EXISTENT_HOUSE(5030, "존재하지 않은 매물입니다."),
    NON_EXISTENT_HOUSELIST(5031, "매물 목록이 존재하지 않습니다."),
    NON_EXISTENT_HOUSEOPTION(5032, "매물 옵션이 존재하지 않습니다."),
    NON_EXISTENT_DEAL_COMPLETED(5033, "해당 매물은 이미 거래가 완료되었습니다."),

    //MATCH
    NON_EXISTENT_MATCH(5033, "관심 매물이 존재하지 않습니다."),

    //WALLET
    NON_EXISTENT_WALLET(5034, "지갑 주소가 존재하지 않습니다"),

    // 권한
    UNAUTHORIZED_ACCESS(8000, "접근 권한이 없습니다.");

    private final int code;
    private final String message;

}
