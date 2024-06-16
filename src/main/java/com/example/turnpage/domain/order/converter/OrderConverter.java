package com.example.turnpage.domain.order.converter;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.order.dto.OrderResponse;
import com.example.turnpage.domain.order.dto.OrderResponse.SimpleOrderInfo;
import com.example.turnpage.domain.order.entity.Order;
import com.example.turnpage.domain.salePost.entity.SalePost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class OrderConverter {

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
}
