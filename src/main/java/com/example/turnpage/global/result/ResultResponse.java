package com.example.turnpage.global.result;

import lombok.Getter;

@Getter
public class ResultResponse<T> {
    private final int status;
    private final String code;
    private final String message;
    private T data;
    public ResultResponse(ResultCode resultCode, T data) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public static <T> ResultResponse<T> of(ResultCode resultCode, T data) {
        return new ResultResponse<>(resultCode, data);
    }

    public static <T> ResultResponse<T> of(ResultCode resultCode) {
        return new ResultResponse<>(resultCode, null);
    }
}
