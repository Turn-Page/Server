package com.example.turnpage.domain.report.converter;

import com.example.turnpage.domain.book.converter.BookConverter;
import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.report.dto.ReportRequest.PostReportRequest;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportId;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportInfo;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportInfo.WriterInfo;
import com.example.turnpage.domain.report.entity.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ReportConverter {
    private final BookConverter bookConverter;

    public Report toEntity(PostReportRequest request, Book book, Member member) {
        return Report.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .book(book)
                .member(member)
                .build();

    }

    public ReportId toReportId(Long reportId) {
        return new ReportId(reportId);
    }

    public List<ReportInfo> toReportInfoList(List<Report> reportList) {
        return reportList
                .stream()
                .map(report -> toReportInfo(report))
                .collect(Collectors.toList());
    }

    public ReportInfo toReportInfo(Report report) {
        return ReportInfo.builder()
                .reportId(report.getId())
                .title(report.getTitle())
                .content(report.getContent())
                .startDate(report.getStartDate())
                .endDate(report.getEndDate())
                .bookInfo(bookConverter.tokBookInfo(report.getBook()))
                .writerInfo(this.toWriterInfo(report.getMember()))
                .build();
    }

    public WriterInfo toWriterInfo(Member member) {
        return WriterInfo.builder()
                .writer(member.getName())
                .profileImage(member.getImage())
                .build();
    }
}
