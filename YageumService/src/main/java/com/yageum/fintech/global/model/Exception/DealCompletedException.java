package com.yageum.fintech.global.model.Exception;

public class DealCompletedException extends RuntimeException {
    private final ExceptionList exceptionList;

    public DealCompletedException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
