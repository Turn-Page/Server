package com.example.turnpage.domain.report.service;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.report.dto.ReportRequest;
import com.example.turnpage.domain.report.dto.ReportResponse;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportId;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportInfo;

import java.util.List;

public interface ReportService {
    ReportId postReport(Member member, ReportRequest.PostReportRequest request);

    List<ReportInfo> findMyReportList(Member member);

    List<ReportInfo> findFriendsReportList(Member member);
}
