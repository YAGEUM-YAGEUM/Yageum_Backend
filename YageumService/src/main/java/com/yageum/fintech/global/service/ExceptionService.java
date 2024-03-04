package com.yageum.fintech.global.service;

import com.mju.management.global.model.Exception.*;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.model.Exception.*;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionService {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult unknown(Exception e){
        log.error("unknown exception", e);
        return responseService.getFailResult(ExceptionList.UNKNOWN.getCode(), ExceptionList.UNKNOWN.getMessage());
    }

    @ExceptionHandler({NonExistentException.class})
    protected CommonResult handleCustom(NonExistentException e) {
        log.error("non existent exception", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(StartDateAfterEndDateException.class)
    protected CommonResult startDateAfterEndDateException(StartDateAfterEndDateException e) {
        log.error("start date after end date exception", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(InvalidDateFormatException.class)
    protected CommonResult invalidDateFormatException(InvalidDateFormatException e) {
        log.error("invalid date format exception", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(OutOfProjectScheduleRangeException.class)
    protected CommonResult outOfProjectScheduleRangeException(OutOfProjectScheduleRangeException e) {
        log.error("out of project's schedule range exception", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(NullJwtTokenException.class)
    protected CommonResult nullJwtTokenException(NullJwtTokenException e) {
        log.error("null jwt token exception", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected CommonResult expiredJwtExceptionException(ExpiredJwtException e) {
        log.error("expired jwt exception", e);
        ExceptionList exceptionList = ExceptionList.EXPIRED_JWT;
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(PrematureJwtException.class)
    protected CommonResult prematureJwtException(PrematureJwtException e) {
        log.error("premature jwt exception", e);
        ExceptionList exceptionList = ExceptionList.PREMATURE_JWT;
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    protected CommonResult unsupportedJwtException(UnsupportedJwtException e) {
        log.error("unsupported jwt exception", e);
        ExceptionList exceptionList = ExceptionList.UNSUPPORTED_JWT;
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    protected CommonResult malformedJwtException(MalformedJwtException e) {
        log.error("malformed jwt exception", e);
        ExceptionList exceptionList = ExceptionList.MALFORMED_JWT;
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(SignatureException.class)
    protected CommonResult signatureException(SignatureException e) {
        log.error("signature jwt exception", e);
        ExceptionList exceptionList = ExceptionList.SIGNATURE_JWT;
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    protected CommonResult securityException(SecurityException e) {
        log.error("security exception", e);
        ExceptionList exceptionList = ExceptionList.SECURITY_JWT;
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected CommonResult illegalArgumentException(IllegalArgumentException e) {
        log.error("illegal argument exception", e);
        ExceptionList exceptionList = ExceptionList.ILLEGAL_ARGUMENT_JWT;
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    protected CommonResult unauthorizedAccessException(UnauthorizedAccessException e) {
        log.error("unauthorized access exception", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

}
