package com.example.turnpage.global.error;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCodeInterface errorCode;
    private final List<ErrorResponse.FieldError> errors = new ArrayList<>();

    public BusinessException(String message, ErrorCodeInterface errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCodeInterface errorCode) {
        super(errorCode.getErrorCode().getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return this.errorCode.getErrorCode();
    }

}