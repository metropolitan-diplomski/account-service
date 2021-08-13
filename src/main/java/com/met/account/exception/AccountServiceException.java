package com.met.account.exception;

public class AccountServiceException extends RuntimeException {

    private ErrorCode errorCode;

    public AccountServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
