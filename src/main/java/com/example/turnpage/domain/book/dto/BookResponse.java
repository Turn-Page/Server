package com.example.turnpage.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

public class BookResponse {

    @Getter
    @AllArgsConstructor
    public static class BookId {
        private Long bookId;
    }

    @Getter
    @SuperBuilder
    public static class BestSellerInfo {
        private String title;
        private String author;
        private String cover;
        private String isbn;
        private String publisher;
        private String publicationDate;
        private String description;
        private Integer rank;
    }

    @Getter
    @SuperBuilder
    public static class BookInfo extends BestSellerInfo {
        private Double star;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookPageInfos {
        private List<BestSellerInfo> bestSellerInfos = new ArrayList<>();
        private int page;
        private int totalPages;
        private int totalBooks;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
