package com.iv.constant;

public class BusException extends Exception {

    private ErrorMsg errorMsg;

    public BusException(ErrorMsg errorMsg) {
        super(errorMsg.toString());
        this.errorMsg = errorMsg;
    }

    public ErrorMsg getErrorMsg(){
        return errorMsg;
    }
}