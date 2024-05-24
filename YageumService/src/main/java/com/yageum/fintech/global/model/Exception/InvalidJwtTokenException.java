package com.yageum.fintech.global.model.Exception;

public class InvalidJwtTokenException extends RuntimeException {
    private final ExceptionList exceptionList;

    public InvalidJwtTokenException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
