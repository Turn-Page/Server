package com.example.turnpage.global.error.code;

import com.example.turnpage.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    EMPTY_JWT(401, "AUTH001", "JWT가 없습니다."),
    EXPIRED_MEMBER_JWT(401, "AUTH002", "만료된 JWT입니다."),
    UNSUPPORTED_JWT(401, "AUTH003", "지원하지 않는 JWT입니다."),
    INVALID_ID_TOKEN(400, "AUTH004", "유효하지 않은 ID TOKEN입니다."),
    INVALID_ACCESS_TOKEN(400, "AUTH005", "유효하지 않은 ACCESS TOKEN입니다."),
    INVALID_REFRESH_TOKEN(400, "AUTH006", "유효하지 않은 REFRESH TOKEN입니다."),
    FAILED_SOCIAL_LOGIN(500, "AUTH007", "소셜 로그인에 실패하였습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
