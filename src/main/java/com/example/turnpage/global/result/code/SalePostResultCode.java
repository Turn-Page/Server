package com.example.turnpage.global.result.code;

import com.example.turnpage.global.result.ResultCode;
import com.example.turnpage.global.result.ResultCodeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SalePostResultCode implements ResultCodeInterface {
    SAVE_SALE_POST(200, "SS001", "판매글을 성공적으로 저장하였습니다."),

    ;
    private final int status;
    private final String code;
    private final String message;

    @Override
    public ResultCode getResultCode() {
        return ResultCode.builder()
                .status(this.status)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
