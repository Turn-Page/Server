package com.example.turnpage.global.result.code;

import com.example.turnpage.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SalePostResultCode implements ResultCode {
    SAVE_SALE_POST(200, "SS001", "판매글을 성공적으로 저장하였습니다."),
    EDIT_SALE_POST(200,"SS002", "판매글을 성공적으로 수정하였습니다."),
    DELETE_SALE_POST(200, "SS003", "판매글을 성공적으로 삭제하였습니다."),
    SALE_POST_LIST(200, "SS004", "판매글 목록을 성공적으로 조회하였습니다."),
    SEARCH_SALE_POST(200,"SS005", "키워드를 성공적으로 검색하였습니다."),
    SALE_POST_DETAIL(200, "SS006", "판매글 상세를 성공적으로 조회하였습니다.")

    ;
    private final int status;
    private final String code;
    private final String message;
}
