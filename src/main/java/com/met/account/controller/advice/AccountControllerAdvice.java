package com.met.account.controller.advice;

import com.met.account.exception.AccountServiceException;
import com.met.account.exception.ErrorCode;
import com.met.account.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

@ControllerAdvice
public class AccountControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {AccountServiceException.class})
    protected ResponseEntity<ErrorResponse> handleNotFound(
            AccountServiceException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value
            = {IllegalArgumentException.class})
    protected ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ILLEGAL_ARGUMENT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value
            = {ValidationException.class})
    protected ResponseEntity<ErrorResponse> handleNotFound(
            ValidationException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_EXCEPTION, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
