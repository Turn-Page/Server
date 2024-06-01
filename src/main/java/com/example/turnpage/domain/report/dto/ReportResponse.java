package com.example.turnpage.domain.report.dto;

import com.example.turnpage.domain.book.dto.BookResponse.BookInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.turnpage.domain.member.dto.MemberResponse.MemberInfo;

public abstract class ReportResponse {

    @Getter
    @AllArgsConstructor
    public static class ReportId {
        private Long reportId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ReportInfo {
        private Long reportId;
        private String title;
        private String content;
        private LocalDate startDate;
        private LocalDate endDate;
        private BookInfo bookInfo;
        private MemberInfo memberInfo;
        private LocalDate createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PagedReportList {
        @Builder.Default
        private List<ReportInfo> reportList= new ArrayList<>();
        private int page;
        private int totalPages;
        private long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
