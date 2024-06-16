package com.example.turnpage.global.result.code;

import com.example.turnpage.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderResultCode implements ResultCode {
    CREATE_ORDER(200, "SO001", "해당 도서에 대한 주문이 성공적으로 완료되었습니다."),
    MY_ORDER_LIST(200, "SO002", "내 주문 목록을 성공적으로 조회하였습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
