package com.example.turnpage.domain.report.dto;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.report.dto.validation.DateRange;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

public abstract class ReportRequest {
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @DateRange
    public static class PostReportRequest extends EditReportRequest {
        @NotBlank
        private SaveBookRequest bookInfo;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @DateRange
    public static class EditReportRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        @NotBlank
        private LocalDate startDate;
        @NotBlank
        private LocalDate endDate;
    }
}
