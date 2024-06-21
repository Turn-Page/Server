package com.example.turnpage.global.error.code;

import com.example.turnpage.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements ErrorCode {
    REPORT_NOT_FOUND(404, "ER001", "해당 reportId를 가진 독후감이 존재하지 않습니다."),
    WRITER_ONLY_MODIFY_REPORT(401, "ER002", "해당 독후감의 작성자만 수정하거나 삭제할 수 있습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
