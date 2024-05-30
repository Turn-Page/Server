package com.example.turnpage.global.error.domain;

import com.example.turnpage.global.error.ErrorCode;
import com.example.turnpage.global.error.ErrorCodeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements ErrorCodeInterface {
    REPORT_NOT_FOUND(404, "ER001", "해당 reportId를 가진 독후감이 존재하지 않습니다."),
    WRITER_ONLY_MODIFY_REPORT(401, "ER002", "해당 독후감의 작성자만 수정할 수 있습니다."),

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
