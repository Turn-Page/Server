package com.example.turnpage.domain.salePost.dto;

import com.example.turnpage.domain.book.dto.BookResponse.SimpleBookInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalePostResponse {

    @Getter
    @AllArgsConstructor
    public static class SalePostId {
        private Long salePostId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalePostInfo {
        private Long salePostId;
        private SimpleBookInfo bookInfo;
        private String title;
        private Integer price;
        private String grade;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedSalePostInfo {
        @Builder.Default
        private List<SalePostInfo> salePostInfoList = new ArrayList<>();
        private int page;
        private int totalPages;
        private int totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
