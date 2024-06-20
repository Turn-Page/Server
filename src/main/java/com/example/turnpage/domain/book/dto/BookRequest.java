package com.example.turnpage.domain.book.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
        @NotNull
        private Long itemId;
        @NotEmpty
        private String title;
        @NotEmpty
        private String author;
        @NotEmpty
        private String cover;
        @NotEmpty
        private String isbn;
        private String publisher;
        private String publicationDate;
        private String description;
        private Integer rank;
    }
}
