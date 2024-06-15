package com.example.turnpage.global.error.code;

import com.example.turnpage.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    SALEPOST_IS_ALREADY_SOLD(400, "EO000", "해당 판매 게시글은 이미 판매 완료된 글입니다."),
    NOT_ENOUGH_POINT_TO_ORDER(400, "EO000", "회원의 포인트가 부족합니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
