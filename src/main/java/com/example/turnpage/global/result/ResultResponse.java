package com.example.turnpage.global.result;

import lombok.Getter;

@Getter
public class ResultResponse<T> {
    private final ResultCodeInterface resultCode;
    private T data;
    public ResultResponse(ResultCodeInterface resultCode, T data) {
        this.resultCode = resultCode;
        this.data = data;
    }

    public static <T> ResultResponse<T> of(ResultCodeInterface resultCode, T data) {
        return new ResultResponse<>(resultCode, data);
    }

    public static <T> ResultResponse<T> of(ResultCodeInterface resultCode) {
        return new ResultResponse<>(resultCode, null);
    }

    public ResultCode getResultCode() {
        return this.resultCode.getResultCode();
    }
}