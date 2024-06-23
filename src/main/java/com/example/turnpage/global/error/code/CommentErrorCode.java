package com.example.turnpage.global.error.code;

import com.example.turnpage.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    COMMENT_NOT_FOUND(404, "EF001", "해당 commentId를 가진 리뷰가 존재하지 않습니다."),
    WRITER_ONLY_DELETE_COMMENT(401, "EC002", "작성자만 리뷰를 삭제할 수 있습니다.")

    ;

    private final int status;
    private final String code;
    private final String message;
}
