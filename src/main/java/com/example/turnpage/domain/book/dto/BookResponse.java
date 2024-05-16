package com.example.turnpage.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookId {
        private Long bookId;
    }
}
