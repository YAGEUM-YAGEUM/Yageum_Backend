package com.yageum.fintech.global.model.Exception;

public class UnauthorizedAccessException extends RuntimeException{

    private final ExceptionList exceptionList;

    public UnauthorizedAccessException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
