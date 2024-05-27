package com.example.turnpage.domain.report.controller;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.report.dto.ReportRequest.PostReportRequest;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportId;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportInfo;
import com.example.turnpage.domain.report.service.ReportService;
import com.example.turnpage.global.config.security.LoginMember;
import com.example.turnpage.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.turnpage.global.result.code.ReportResultcode.*;


@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/reports")
    public ResultResponse<ReportId> postReport(@LoginMember Member member,
                                               PostReportRequest request) {
        return ResultResponse.of(POST_REPORT.getResultCode(), reportService.postReport(member, request));
    }

    @GetMapping("/reports/my")
    public ResultResponse<List<ReportInfo>> findMyReportList(@LoginMember Member member) {
        return ResultResponse.of(MY_REPORT_LIST.getResultCode(), reportService.findMyReportList(member));
    }

    @GetMapping("/reports/friends")
    ResultResponse<List<ReportInfo>> findFriendsReportList(@LoginMember Member member) {
        return ResultResponse.of(FRIENDS_REPORT_LIST.getResultCode(), reportService.findFriendsReportList(member));
    }
}
