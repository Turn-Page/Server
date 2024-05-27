package com.example.turnpage.domain.report.dto;

import com.example.turnpage.domain.book.dto.BookRequest;
import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public abstract class ReportRequest {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostReportRequest {
        private String title;
        private String content;
        private LocalDate startDate;
        private LocalDate endDate;
        private SaveBookRequest bookInfo;
    }


}
