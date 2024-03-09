package com.yageum.fintech.global.model.Exception;

import lombok.Getter;

public class BusinessLogicException extends RuntimeException {

    @Getter
    private ExceptionList exceptionList;

    public BusinessLogicException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }
}
