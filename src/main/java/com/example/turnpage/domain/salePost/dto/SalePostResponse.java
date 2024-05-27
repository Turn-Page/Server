package com.example.turnpage.domain.salePost.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SalePostResponse {

    @Getter
    @AllArgsConstructor
    public static class SalePostId {
        private Long salePostId;
    }
}
