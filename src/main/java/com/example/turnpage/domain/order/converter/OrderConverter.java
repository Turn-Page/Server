package com.example.turnpage.domain.order.converter;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.order.dto.OrderResponse;
import com.example.turnpage.domain.order.dto.OrderResponse.DetailedOrderInfo;
import com.example.turnpage.domain.order.dto.OrderResponse.PagedOrderInfo;
import com.example.turnpage.domain.order.dto.OrderResponse.SimpleOrderInfo;
import com.example.turnpage.domain.order.entity.Order;
import com.example.turnpage.domain.salePost.converter.SalePostConverter;
import com.example.turnpage.domain.salePost.entity.SalePost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class OrderConverter {
    private final SalePostConverter salePostConverter;

    public Order toEntity(Member member, SalePost salePost) {
        // 겹치지 않는 주문 번호 생성 로직
        return Order.builder()
                .id(null)
                .member(member)
                .salePost(salePost)
                .orderNumber(null) // 서비스 로직에서 설정
                .orderedAt(LocalDateTime.now())
                .build();
    }

    public SimpleOrderInfo toSimpleOrderInfo(Order order) {
        return SimpleOrderInfo.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .build();
    }

    public DetailedOrderInfo toDetailedOrderInfo(Order order) {
        return DetailedOrderInfo.builder()
                .orderId(order.getId())
                .salePostInfo(salePostConverter.toSalePostInfo(order.getSalePost()))
                .orderNumber(order.getOrderNumber())
                .orderedAt(order.getOrderedAt())
                .build();
    }

    public PagedOrderInfo toPagedOrderInfo(Page<Order> orders) {
        List<DetailedOrderInfo> orderInfoList = orders.stream()
                .map(order -> toDetailedOrderInfo(order))
                .collect(Collectors.toList());

        return PagedOrderInfo.builder()
                .orderInfoList(orderInfoList)
                .page(orders.getNumber())
                .totalPages(orders.getTotalPages())
                .totalElements(orders.getTotalElements())
                .isFirst(orders.isFirst())
                .isLast(orders.isLast())
                .build();
    }

}
