package com.example.turnpage.global.error.domain;

import com.example.turnpage.global.error.ErrorCode;
import com.example.turnpage.global.error.ErrorCodeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SalePostErrorCode implements ErrorCodeInterface {
    SALE_POST_NOT_FOUND(400, "ES001", "해당 salePostId를 가진 판매글이 존재하지 않습니다."),
    INVALID_GRADE_INPUT(400,"ES002", "GRADE enum값이 올바르지 않습니다. 최상, 상, 중, 하 중에 선택해주세요."),
    NO_AUTHORIZATION_SALE_POST(400, "ES003", "해당 판매글에 대한 수정,삭제 권한이 없습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.builder()
                .status(this.status)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
