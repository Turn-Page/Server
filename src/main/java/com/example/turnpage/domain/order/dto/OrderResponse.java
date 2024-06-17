package com.example.turnpage.domain.order.dto;


import com.example.turnpage.domain.report.dto.ReportResponse;
import com.example.turnpage.domain.salePost.dto.SalePostResponse;
import com.example.turnpage.domain.salePost.dto.SalePostResponse.SalePostInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class OrderResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SimpleOrderInfo {
        private Long orderId;
        private String orderNumber;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DetailedOrderInfo {
        private Long orderId;
        private String orderNumber;
        private SalePostInfo salePostInfo;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime orderedAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PagedOrderInfo {
        @Builder.Default
        private List<DetailedOrderInfo> orderInfoList = new ArrayList<>();
        private int page;
        private int totalPages;
        private long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
