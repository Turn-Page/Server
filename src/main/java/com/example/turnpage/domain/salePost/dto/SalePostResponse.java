package com.example.turnpage.domain.salePost.dto;

import com.example.turnpage.domain.book.dto.BookResponse;
import com.example.turnpage.domain.book.dto.BookResponse.BookInfo;
import com.example.turnpage.domain.book.dto.BookResponse.SimpleBookInfo;
import com.example.turnpage.domain.member.dto.MemberResponse;
import com.example.turnpage.domain.member.dto.MemberResponse.MemberInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class SalePostResponse {

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
    public static class SalePostDetailInfo {
        private Long salePostId;
        private BookInfo bookInfo;
        private MemberInfo memberInfo;
        private String title;
        private Integer price;
        private String grade;
        private String description;
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
        private long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
