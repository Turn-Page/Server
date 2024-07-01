package com.example.turnpage.global.result.code;

import com.example.turnpage.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportResultcode implements ResultCode {
    POST_REPORT(200, "SR001", "새 독후감을 성공적으로 게시하였습니다."),
    MY_REPORT_LIST(200, "SR002", "내 독후감 목록을 성공적으로 조회하였습니다."),
    FRIENDS_REPORT_LIST(200, "SR003", "친구들의 독후감 목록을 성공적으로 조회하였습니다."),
    SEARCH_REPORT_LIST(200, "SR000", "입력한 문자열로 독후감 목록을 성공적으로 검색하였습니다."),
    GET_SPECIFIC_REPORT(200, "SR004", "주어진 reportId에 대응하는 독후감을 성공적으로 조회하였습니다."),
    UPDATE_SPECIFIC_REPORT(200, "SR005", "주어진 reportId에 대응하는 독후감을 성공적으로 수정하였습니다."),
    DELETE_SPECIFIC_REPORT(200, "SR006", "주어진 reportId에 대응하는 독후감을 성공적으로 삭제하였습니다."),


    ;
    private final int status;
    private final String code;
    private final String message;
}
