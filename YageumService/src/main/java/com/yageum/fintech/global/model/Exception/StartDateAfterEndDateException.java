package com.yageum.fintech.global.model.Exception;

public class StartDateAfterEndDateException extends RuntimeException{
    private final ExceptionList exceptionList;

    public StartDateAfterEndDateException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
