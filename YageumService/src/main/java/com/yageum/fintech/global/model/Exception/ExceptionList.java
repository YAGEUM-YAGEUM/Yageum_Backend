package com.yageum.fintech.global.model.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ExceptionList {

    UNKNOWN(-9999, "알 수 없는 오류가 발생하였습니다."),

    //Tenant, Lessor
    NON_EXISTENT_CHECKLIST(5005, "내용이 존재하지 않습니다."),
    INVALID_USER_ID(5011, "요청으로 들어온 User의 식별자가 유효하지 않습니다."),

    BAD_REQUEST(400, "잘못된 요청입니다"),
    TOKEN_IS_NOT_SAME(401, "액세스 토큰과 리프레시 토큰이 일치하지 않습니다"),
    HEADER_REFRESH_TOKEN_NOT_EXISTS(400, "요청 헤더에 리프레시 토큰이 누락되었습니다"),
    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다"),
    MEMBER_EXISTS(409, "이미 존재하는 회원입니다"),
    ALREADY_EXISTS(409, "이미 존재하는 아이디입니다"),
    NO_SUCH_ALGORITHM(500, "존재하지 않는 알고리즘입니다"),
    UNABLE_TO_SEND_EMAIL(500, "이메일을 전송할 수 없습니다"),

    // Jwt
    NULL_JWT_TOKEN(7000, "토큰이 존재하지 않습니다."),
    EXPIRED_JWT(7001, "만료된 토큰입니다."),
    PREMATURE_JWT(7002, "아직 활성화되지 않은 토큰입니다."),
    UNSUPPORTED_JWT(7003, "지원되지 않는 토큰 형식 또는 구조입니다."),
    MALFORMED_JWT(7004, "토큰의 형식이 잘못되었습니다."),
    SIGNATURE_JWT(7005, "토큰의 서명이 검증되지 않았습니다."),
    ILLEGAL_ARGUMENT_JWT(7006, "잘못된 인자가 전달되었습니다."),
    SECURITY_JWT(7007, "보안 관련 문제로 토큰 검증이 실패했습니다."),

    // SCHEDULE
    NON_EXISTENT_SCHEDULE(6000, "일정이 존재하지 않습니다."),
    NON_EXISTENT_SCHEDULELIST(6001, "일정 목록이 존재하지 않습니다."),
    START_DATE_AFTER_END_DATE_EXCEPTION(6002, "시작일이 종료일의 이후입니다."),
    INVALID_DATE_FORMAT(6003, "유효하지 않은 날짜 형태입니다."),
    OUT_OF_PROJECT_SCHEDULE_RANGE(6004, "프로젝트의 일정 범위를 벗어났습니다."),

    NON_EXISTENT_PROJECT(5008, "프로젝트가 존재하지 않습니다."),
    INVALID_INPUT_VALUE(5009, "유효성 검사에 실패하였습니다."),

    // PROJECT
    INVALID_PROJECT_ID(5010, "요청으로 들어온 Project의 식별자가 유효하지 않습니다."),
    NON_EXISTENT_TENANT(5010, "존재하지 않는 임차인입니다."),
    NON_EXISTENT_TENANT_PROFILE(5010, "임차인 프로필이 존재하지 않습니다"),
    NON_EXISTENT_LESSOR(5010, "존재하지 않는 임대인입니다."),
    NON_EXISTENT_LESSOR_PROFILE(5010, "임대인 프로필이 존재하지 않습니다"),
    TENANT_PROFILE_ALREADY_EXISTS(5010, "이미 해당 tenantId에 대한 프로필이 존재합니다."),

    // PROJECT & TEAM
    NOT_TEAM_MEMBER(5012, "요청으로 들어온 User는 해당 Project의 팀원이 아닙니다."),

    // POST
    INVALID_POST_ID(5013, "요청으로 들어온 Post의 식별자가 유효하지 않습니다."),
    NO_PERMISSION_TO_EDIT_POST(5014, "게시글 수정 권한이 없습니다."),

    // Comment
    NON_EXISTENT_COMMENT(5020, "존재하지 않는 댓글입니다."),
    NON_EXISTENT_CATEGORY(5021, "존재하지 않는 카테고리입니다."),

    //HOUSE
    NON_EXISTENT_HOUSE(5030, "존재하지 않은 매물입니다."),
    NON_EXISTENT_HOUSELIST(5031, "매물 목록이 존재하지 않습니다."),

    // 권한
    UNAUTHORIZED_ACCESS(8000, "접근 권한이 없습니다.");

    private final int code;
    private final String message;

}
