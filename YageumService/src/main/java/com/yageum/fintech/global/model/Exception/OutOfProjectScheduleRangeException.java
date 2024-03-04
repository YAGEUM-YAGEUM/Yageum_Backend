package com.yageum.fintech.global.model.Exception;

public class OutOfProjectScheduleRangeException extends RuntimeException{

    private final ExceptionList exceptionList;

    public OutOfProjectScheduleRangeException(ExceptionList exceptionList){
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList(){
        return exceptionList;
    }
}
