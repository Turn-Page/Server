package com.example.turnpage.global.error.domain;

import com.example.turnpage.global.error.ErrorCode;
import com.example.turnpage.global.error.ErrorCodeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowErrorCode implements ErrorCodeInterface {
    FOLLOW_NOT_FOUND(404, "EF001", "회원님은 해당 memberId를 가진 회원을 팔로우하고 있지 않습니다."),
    CANNOT_FOLLOW_MYSELF(400, "EF002", "자기 자신을 팔로우할 수 없습니다."),

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
