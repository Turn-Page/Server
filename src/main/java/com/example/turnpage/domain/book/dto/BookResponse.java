package com.example.turnpage.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookId {
        private Long bookId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
}
