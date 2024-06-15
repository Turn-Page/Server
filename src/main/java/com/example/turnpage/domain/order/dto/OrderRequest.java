package com.example.turnpage.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public abstract class OrderRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateOrderRequest {
        @NotNull(message = "주문할 상품의 판매 게시글 id를 입력해 주세요.")
        private Long salePostId;
    }
}
