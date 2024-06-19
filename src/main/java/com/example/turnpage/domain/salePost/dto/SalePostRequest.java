package com.example.turnpage.domain.salePost.dto;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
        @NotEmpty
        private String title;
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
        @NotEmpty
        private String title;
        @NotEmpty
        private String description;
        @NotBlank
        private String grade;
        @Positive
        private Integer price;
    }
}
