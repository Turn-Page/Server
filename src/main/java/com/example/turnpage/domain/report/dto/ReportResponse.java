package com.example.turnpage.domain.report.dto;

import com.example.turnpage.domain.book.dto.BookResponse.BookInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import static com.example.turnpage.domain.member.dto.MemberResponse.WriterInfo;

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
        private WriterInfo writerInfo;
    }
}
