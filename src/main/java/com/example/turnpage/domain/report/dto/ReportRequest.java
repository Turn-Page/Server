package com.example.turnpage.domain.report.dto;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.report.dto.validation.DateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public abstract class ReportRequest {
    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostReportRequest extends EditReportRequest {
        @NotNull
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
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull
        private LocalDate startDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull
        private LocalDate endDate;
    }
}
