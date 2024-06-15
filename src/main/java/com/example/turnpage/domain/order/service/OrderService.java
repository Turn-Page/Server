package com.example.turnpage.domain.order.service;


import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.order.dto.OrderRequest.CreateOrderRequest;
import com.example.turnpage.domain.order.dto.OrderResponse.SimpleOrderInfo;

public interface OrderService {
    SimpleOrderInfo createOrder(Member member, CreateOrderRequest request);
}
