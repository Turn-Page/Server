package com.example.turnpage.global.error.code;

import com.example.turnpage.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowErrorCode implements ErrorCode {
    FOLLOW_NOT_FOUND(404, "EF001", "회원님은 해당 memberId를 가진 회원을 팔로우하고 있지 않습니다."),
    CANNOT_FOLLOW_MYSELF(400, "EF002", "자기 자신을 팔로우할 수 없습니다."),
    ALREADY_FOLLOWING(400, "EF003", "해당 회원을 이미 팔로우하고 있습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
