package com.example.turnpage.global.result;

import lombok.Getter;

@Getter
public class ResultResponse<T> {
    private int status;
    private String code;
    private String message;
    private T data;
    public ResultResponse(ResultCodeInterface resultCode, T data) {
        this.status = resultCode.getResultCode().getStatus();
        this.code = resultCode.getResultCode().getCode();
        this.message = resultCode.getResultCode().getMessage();
        this.data = data;
    }

    public static <T> ResultResponse<T> of(ResultCodeInterface resultCode, T data) {
        return new ResultResponse<>(resultCode, data);
    }

    public static <T> ResultResponse<T> of(ResultCodeInterface resultCode) {
        return new ResultResponse<>(resultCode, null);
    }
}