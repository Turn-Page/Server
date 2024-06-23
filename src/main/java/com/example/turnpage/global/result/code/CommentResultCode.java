package com.example.turnpage.global.result.code;

import com.example.turnpage.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentResultCode implements ResultCode {

    WRITE_COMMENT(200, "SF001", "성공적으로 해당 도서에 새 리뷰를 작성하였습니다."),
    SPECIFIC_BOOK_COMMENT_LIST(200, "SF002", "성공적으로 해당 bookId를 가진 도서의 리뷰 목록을 조회하였습니다."),
    DELETE_COMMENT(200, "SC003", "성공적으로 해당 commentId를 가진 리뷰를 삭제하였습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
