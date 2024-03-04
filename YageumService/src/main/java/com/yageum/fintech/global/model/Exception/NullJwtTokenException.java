package com.yageum.fintech.global.model.Exception;

public class NullJwtTokenException extends RuntimeException{
    private final ExceptionList exceptionList;

    public NullJwtTokenException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
