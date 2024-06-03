package com.example.turnpage.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class BookRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveBookRequest {
        private Long itemId;
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
