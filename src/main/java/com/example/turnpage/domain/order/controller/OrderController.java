package com.example.turnpage.domain.order.controller;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.order.dto.OrderRequest.CreateOrderRequest;
import com.example.turnpage.domain.order.service.OrderService;
import com.example.turnpage.global.config.security.LoginMember;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.turnpage.global.result.code.OrderResultCode.CREATE_ORDER;

@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "주문 API", description = "주문 관련 API입니다.")
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResultResponse<Object> createOrder(@LoginMember Member member, @RequestBody @Valid CreateOrderRequest request) {
        return ResultResponse.of(CREATE_ORDER, orderService.createOrder(member, request));
    }

}
