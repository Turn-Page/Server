package com.example.turnpage.domain.salePost.dto;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.salePost.entity.Grade;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SalePostRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveSalePostRequest {
        private String title;
        private String description;
        private String grade;
        private Integer price;
        @JsonProperty("bookInfo")
        private SaveBookRequest bookInfo;
    }
}
