package com.example.turnpage.domain.report.dto;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import com.example.turnpage.domain.book.dto.BookResponse.BookInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
        // 독후감 작성자. Member 객체를 넘겨야 하나? 쓸 건 이름 밖에 없는데
        private WriterInfo writerInfo;

        @Getter
        @Builder
        @AllArgsConstructor
        public static class WriterInfo {
            private String writer;
            private String profileImage;
        }
    }
}
