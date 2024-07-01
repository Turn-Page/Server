package com.example.turnpage.domain.report.dto;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.report.dto.validation.DateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
        @Size(max = 30, message = "독후감 제목은 최대 30자까지 작성할 수 있습니다.")
        @NotBlank
        private String title;
        @Size(max = 5000, message = "독후감 내용은 최대 5000자까지 작성할 수 있습니다.")
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
