package com.example.turnpage.global.error.code;

import com.example.turnpage.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookErrorCode implements ErrorCode {
    BOOK_NOT_FOUND(400, "EB001", "해당 bookId 가진 책이 존재하지 않습니다."),
    BOOK_JSON_PARSE_ERROR(400, "EB002", "BOOK open API 결과 json을 파싱할 수 없습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
