package com.example.turnpage.global.error.domain;

import com.example.turnpage.global.error.ErrorCode;
import com.example.turnpage.global.error.ErrorCodeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCodeInterface {
    INTERNAL_SERVER_ERROR(500, "EG001", "내부 서버 오류입니다."),
    METHOD_NOT_ALLOWED(405, "EG002", "허용되지 않은 HTTP method입니다."),
    NETWORK_NOT_CONNECTED(400, "EG003", "네트워크에 연결되어 있지 않습니다."),
    HTTP_HEADER_INVALID(400, "EG004", "request header가 유효하지 않습니다."),
    INVALID_INPUT(400, "EG005", "올바르지 않은 입력입니다."),
    HTTP_MESSAGE_NOT_READABLE(400, "EG006", "Request message body가 없거나, 값 타입이 올바르지 않습니다."),

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
