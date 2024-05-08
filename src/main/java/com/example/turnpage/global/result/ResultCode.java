package com.example.turnpage.global.result;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 결과 코드에 대한 Enum class
 * 결과 코드 형식
 * 1. 모든 결과 코드는 "S"로 시작한다.
 * 2. 2번째 문자는 결과가 발생한 카테고리를 나타낸다.
 * ex) "M": Member, "SP": SalePost, "B": Book, "R": Report
 *
 */
@Builder
@Getter
@RequiredArgsConstructor
public class ResultCode {
    private final int status;
    private final String code;
    private final String message;
}
