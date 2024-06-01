package com.example.turnpage.domain.report.service;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.report.dto.ReportRequest.EditReportRequest;
import com.example.turnpage.domain.report.dto.ReportRequest.PostReportRequest;
import com.example.turnpage.domain.report.dto.ReportResponse.PagedReportList;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportId;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportInfo;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    ReportId postReport(Member member, PostReportRequest request);
    PagedReportList findMyReportList(Member member, Pageable pageable);
    PagedReportList findReportListOfFollowingMembers(Member member, Pageable pageable);
    ReportInfo getReport(Member member, Long reportId);
    ReportId editReport(Member member, Long reportId, EditReportRequest request);
    ReportId deleteReport(Member member, Long reportId);
}
