package com.example.turnpage.global.result.code;

import com.example.turnpage.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderResultCode implements ResultCode {
    CREATE_ORDER(200, "SO001", "성공적으로 해당 도서에 대한 주문이 완료되었습니다."),
    GET_MY_ORDERS(200, "SO002", "성공적으로 내 주문 목록을 조회하였습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
