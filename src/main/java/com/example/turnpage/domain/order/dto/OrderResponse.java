package com.example.turnpage.domain.order.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public abstract class OrderResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SimpleOrderInfo {
        private Long orderId;
        private String orderNumber;
    }
}
