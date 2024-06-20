package com.example.turnpage.global.error.code;

import com.example.turnpage.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SalePostErrorCode implements ErrorCode {
    SALE_POST_NOT_FOUND(400, "ES001", "해당 salePostId를 가진 판매글이 존재하지 않습니다."),
    INVALID_GRADE_INPUT(400,"ES002", "GRADE enum값이 올바르지 않습니다. 최상, 상, 중, 하 중에 선택해주세요."),
    NO_AUTHORIZATION_SALE_POST(400, "ES003", "해당 판매글에 대한 수정,삭제 권한이 없습니다."),
    SALE_POST_NOT_ALLOWED(400, "ES004", "판매 완료된 게시글은 수정,삭제할 수 없습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
