package com.example.turnpage.global.error;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 에러 코드에 대한 Enum class
 * 에러 코드 형식
 * 1. 모든 에러 코드는 "E"로 시작한다.
 * 2. 2번째 문자는 에러가 발생한 카테고리를 나타낸다.
 * ex) "U": User, "A": Article, "C": Collection, "F": File
 */
@Builder
@Getter
@RequiredArgsConstructor
public class ErrorCode {
    private final int status;
    private final String code;
    private final String message;
}
