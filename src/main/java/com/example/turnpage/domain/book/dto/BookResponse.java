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
    public static class BookInfo {
        private Long bookId;
        private String title;
        private String author;
        private String cover;
        private String publisher;
        private String publicationDate;
        private Integer rank;
        private Double star;
    }

    @Getter
    @SuperBuilder
    public static class BookDetailInfo extends BookInfo {
        private String isbn;
        private String description;
    }

    @Getter
    @Builder
    public static class SimpleBookInfo {
        private Long bookId;
        private String title;
        private String cover;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PagedBookInfo {
        @Builder.Default
        private List<BookInfo> bookInfoList = new ArrayList<>();
        private int page;
        private int totalPages;
        private long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
