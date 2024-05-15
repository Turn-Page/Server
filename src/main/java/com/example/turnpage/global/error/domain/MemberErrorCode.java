package com.example.turnpage.global.error.domain;

import com.example.turnpage.global.error.ErrorCode;
import com.example.turnpage.global.error.ErrorCodeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCodeInterface {
    MEMBER_NOT_FOUND(400, "EM001", "해당 memberId를 가진 회원이 존재하지 않습니다."),
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
