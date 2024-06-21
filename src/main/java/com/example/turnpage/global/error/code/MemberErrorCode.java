package com.example.turnpage.global.error.code;

import com.example.turnpage.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND(404, "EM001", "해당 memberId를 가진 회원이 존재하지 않습니다."),
    MEMBER_NOT_FOUND_BY_EMAIL(404, "EM002", "해당 이메일을 가진 회원이 존재하지 않습니다."),
    CLIENT_REGISTRATION_NOT_FOUND(400, "EM000", "해당 registrationId를 가진 ClientRegistration이 존재하지 않습니다."),
    CANNOT_CHARGE_OVER_MAX_POINT(400, "EM005", "한 번에 최대 충전 포인트보다 많이 충전할 수 없습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
