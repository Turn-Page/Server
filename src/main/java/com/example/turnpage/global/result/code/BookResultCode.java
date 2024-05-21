package com.example.turnpage.global.result.code;

import com.example.turnpage.global.result.ResultCode;
import com.example.turnpage.global.result.ResultCodeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookResultCode implements ResultCodeInterface {
    SAVE_BOOK(200, "SB001", "책 정보를 성공적으로 저장하였습니다."),
    FETCH_BESTSELLER(200, "SB002", "베스트 셀러 목록을 성공적으로 조회하였습니다."),
    BOOK_INFO(200, "SB003", "책 상세 정보를 성공적으로 조회하였습니다."),
    SEARCH_BOOK(200, "SB004", "성공적으로 책을 검색하였습니다."),
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
