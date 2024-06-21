package com.example.turnpage.domain.report.service;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.report.dto.ReportRequest.EditReportRequest;
import com.example.turnpage.domain.report.dto.ReportRequest.PostReportRequest;
import com.example.turnpage.domain.report.dto.ReportResponse.DetailedReportInfo;
import com.example.turnpage.domain.report.dto.ReportResponse.PagedReportInfo;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportId;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    ReportId postReport(Member member, PostReportRequest request);
    PagedReportInfo findMyReportList(Member member, Pageable pageable);
    PagedReportInfo findReportListOfFollowingMembers(Member member, Pageable pageable);
    DetailedReportInfo getReport(Member member, Long reportId);
    ReportId editReport(Member member, Long reportId, EditReportRequest request);
    ReportId deleteReport(Member member, Long reportId);
}
