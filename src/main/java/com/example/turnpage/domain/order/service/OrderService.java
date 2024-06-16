package com.example.turnpage.domain.order.service;


import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.order.dto.OrderRequest.CreateOrderRequest;
import com.example.turnpage.domain.order.dto.OrderResponse.PagedOrderInfo;
import com.example.turnpage.domain.order.dto.OrderResponse.SimpleOrderInfo;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    SimpleOrderInfo createOrder(Member member, CreateOrderRequest request);
    PagedOrderInfo findMyOrderList(Member member, Pageable pageable);
}
