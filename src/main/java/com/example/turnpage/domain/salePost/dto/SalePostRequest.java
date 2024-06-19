package com.example.turnpage.domain.salePost.dto;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class SalePostRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveSalePostRequest {
        @Size(max = 30)
        @NotEmpty
        private String title;
        @Size(max = 1000)
        @NotEmpty
        private String description;
        @NotBlank
        private String grade;
        @Positive
        private Integer price;
        private SaveBookRequest bookInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditSalePostRequest {
        @Size(max = 30)
        @NotEmpty
        private String title;
        @Size(max = 1000)
        @NotEmpty
        private String description;
        @NotBlank
        private String grade;
        @Positive
        private Integer price;
    }
}
